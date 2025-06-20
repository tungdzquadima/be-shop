package com.example.shopapp.controllers;


import com.example.shopapp.dtos.CategoryDTO;
import com.example.shopapp.dtos.ProductDTO;
import com.example.shopapp.dtos.ProductImageDTO;
import com.example.shopapp.models.Product;
import com.example.shopapp.models.ProductImage;
import com.example.shopapp.response.ProductListResponse;
import com.example.shopapp.response.ProductResponse;
import com.example.shopapp.services.IProductService;
import com.github.javafaker.Faker;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final IProductService productService;
    @GetMapping("")
    public ResponseEntity<ProductListResponse> getProducts(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ){

        PageRequest pageRequest=PageRequest.of
                (page, limit, Sort.by("createdAt").descending());
        Page<ProductResponse> productPage=productService.getAllProducts(pageRequest);
        int totalPages = productPage.getTotalPages();// tổng số trang
        List<ProductResponse> products=productPage.getContent();
        return ResponseEntity.ok(ProductListResponse.builder()
                        .products(products)
                        .totalPages(totalPages)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(
            @PathVariable("id") long productId
    ){
        try {
            Product existingProduct=productService.getProductById(productId);
            return ResponseEntity.ok(ProductResponse.fromProduct(existingProduct));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("")
    public ResponseEntity<?> insertAllProducts(@Valid @RequestBody ProductDTO productDTO,
                                                //@ModelAttribute ("files") List<MultipartFile> files,
                                               BindingResult result)  {
        try{
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }

            Product newProduct= productService.createProduct(productDTO);

            return ResponseEntity.ok(newProduct);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body( e.getMessage());
        }

    }
    @PostMapping(value = "uploads/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(
            //@ModelAttribute ("files") List<MultipartFile> files,
            // 1 dòng dưới sửa theo chatgpt
            @RequestParam("files") List<MultipartFile> files,
                                            @PathVariable("id") long productId
    )
    {
        try {
            Product existingProduct=productService.getProductById(productId);
            files = files == null ? new ArrayList<MultipartFile>() :files;
            if (files.size()>ProductImage.MAXIMUM_IMAGES_PER_PRODUCT){
                return ResponseEntity.badRequest().body("you can only upload maximum 5 images");
            }
            List<ProductImage> productImages=new ArrayList<>();
            for (MultipartFile file:files){
                if (file.getSize()==0){
                    continue;
                }
                if(file.getSize()>10*1024*1024){
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                            .body("FILE max = 10mb");
                }
                String contenType= file.getContentType();
                if(contenType==null||!contenType.startsWith("image/")){
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                            .body("File must be an image");
                }
                //lưu file và cập nhật thubnail trong dto
                String fileName=storeFile(file);
                // lưu vào đối tượng product trong db
                ProductImage productImage= productService.createProductImage(existingProduct.getId(),
                        ProductImageDTO
                                .builder()
                                .imageUrl(fileName)
                                .build()

                );
                productImages.add(productImage);

            }
            return ResponseEntity.ok().body(productImages);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }

    }
    private String storeFile(MultipartFile file) throws IOException{

        if(!isImageFile(file) || file.getOriginalFilename()==null){
            throw new IOException("Invalid image format");
        }
        String fileName= StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        // thêm UUID vào trước tên file để đảm bảo tên file là duy nhất
        String uniqueFilename= UUID.randomUUID().toString()+"_"+fileName;
        // đường dẫn đến thư mục mà bận muốn lưu file
        java.nio.file.Path uploadDir= Paths.get("uploads");
        //kiểm tra thư mụa tồn tại chưa
        if (!Files.exists(uploadDir)){
            Files.createDirectories(uploadDir);
        }
        //đường dẫn đầy đủ đến file
        java.nio.file.Path destination=Paths.get(uploadDir.toString(),uniqueFilename);
        Files.copy(file.getInputStream(),destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFilename;
    }

    // kiểm tra xem có phải file ảnh hay không
    private boolean isImageFile(MultipartFile file){
        String contentType=file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateAllProducts(@PathVariable long id, @RequestBody ProductDTO productDTO){
        try{
            Product updateProduct=productService.updateProduct(id, productDTO);
            return ResponseEntity.ok(ProductResponse.fromProduct(updateProduct));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok("Đã xóa sản phẩm với id = " + id);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy sản phẩm với id = " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi xóa sản phẩm.");
        }
    }




     //hàm này thêm nhiều sản phẩm cùng lúc để
    @PostMapping("/themnhieusanpham")
    public ResponseEntity<?> insertAllProducts(@RequestBody List<ProductDTO> productDTOList) {
        try {
            List<Product> createdProducts = new ArrayList<>();
            for (ProductDTO dto : productDTOList) {
                Product product = productService.createProduct(dto);
                createdProducts.add(product);
            }
            return ResponseEntity.ok(createdProducts);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // tìm sản phẩm theo category
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ProductListResponse> getProductsByCategory(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit,
            @PathVariable("categoryId") Long categoryId) {

        PageRequest pageRequest = PageRequest.of(page, limit);
        Page<ProductResponse> productPage = productService.getProductsByCategory(categoryId, pageRequest);
        System.out.println("productPage: "+productPage);
        int totalPages = productPage.getTotalPages();
        List<ProductResponse> products = productPage.getContent();
        System.out.println(categoryId);
        System.out.println(products);
        return ResponseEntity.ok(ProductListResponse.builder()
                .products(products)
                .totalPages(totalPages)
                .build());
    }



    // tìm kếm sản phẩm
    //http://localhost:8088/api/v1/products/search?name=lenovo&page=0&limit=10
    @GetMapping("/search")
    public ResponseEntity<ProductListResponse> searchProductsByName(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "limit", defaultValue = "10") int limit,
            @RequestParam(name = "name", required = false, defaultValue = "") String name
    ) {
        // Validate input
        if (page < 0 || limit <= 0) {
            return ResponseEntity.badRequest().body(
                    ProductListResponse.builder()
                            .products(Collections.emptyList())
                            .totalPages(0)
                            .build()
            );
        }

        // Optionally trim the name
        name = name.trim();

        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("createdAt").descending());

        Page<ProductResponse> productPage = productService.searchProductsByName(name, pageRequest);
        int totalPages = productPage.getTotalPages();
        List<ProductResponse> products = productPage.getContent();

        return ResponseEntity.ok(
                ProductListResponse.builder()
                        .products(products)
                        .totalPages(totalPages)
                        .build()
        );
    }


    // tạo bản ghi fake test chức năng
//    @PostMapping("/fakeProducts")
//    public ResponseEntity<String> fakeProducts() throws Exception {
//        Faker faker=new Faker();
//        for (int i = 0; i < 300; i++) {
//            String productName=faker.commerce().productName();
//            if (productService.existsByName(productName)){
//                continue;
//            }
//            ProductDTO productDTO=ProductDTO.builder()
//                    .name(productName)
//                    .price((float)faker.number().numberBetween(1000,100_000_000))
//                    .description(faker.lorem().sentence())
//                    .categoryId((long)faker.number().numberBetween(100,100))
//                    .thumbnail("")
//                    .build();
//            productService.createProduct(productDTO);
//        }
//            return ResponseEntity.ok("đã tạo thành công danh sách fake product");
//    }

}
