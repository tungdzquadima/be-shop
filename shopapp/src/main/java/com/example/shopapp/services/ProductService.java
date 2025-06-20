package com.example.shopapp.services;

import com.example.shopapp.dtos.ProductDTO;
import com.example.shopapp.dtos.ProductImageDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.exceptions.InvalidParamExeption;
import com.example.shopapp.models.Brand;  // Thêm import Brand
import com.example.shopapp.models.Category;
import com.example.shopapp.models.Product;
import com.example.shopapp.models.ProductImage;
import com.example.shopapp.repositories.BrandRepository;  // Thêm import BrandRepository
import com.example.shopapp.repositories.CategoryRepository;
import com.example.shopapp.repositories.ProductImageReponsitory;
import com.example.shopapp.repositories.ProductRepository;
import com.example.shopapp.response.ProductResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageReponsitory productImageReponsitory;
    private final BrandRepository brandRepository;  // Thêm BrandRepository

    @Override
    public Product createProduct(ProductDTO productDTO) throws DataNotFoundException {

        // Lấy Category từ categoryId
        Category existingCategory = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new DataNotFoundException("cannot find category with id : " + productDTO.getCategoryId()));

        // Lấy Brand từ brandId
        Brand existingBrand = brandRepository.findById(productDTO.getBrandId())  // Thêm tìm thương hiệu
                .orElseThrow(() -> new DataNotFoundException("cannot find brand with id : " + productDTO.getBrandId()));

        // Tạo mới sản phẩm
        Product newProduct = Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .thumbnail(productDTO.getThumbnail())
                .description(productDTO.getDescription())
                .category(existingCategory)
                .brand(existingBrand)
                .build();

        return productRepository.save(newProduct);
    }

    @Override
    public Product getProductById(long productId) throws DataNotFoundException {
        return productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("cannot find product with id: " + productId));
    }

    @Override
    public Page<ProductResponse> getAllProducts(PageRequest pageRequest) {
        // Lấy danh sách sản phẩm theo page và limit
        return productRepository.findAll(pageRequest).map(ProductResponse::fromProduct);
    }

    @Override
    public Product updateProduct(long id, ProductDTO productDTO) throws DataNotFoundException {

        // Lấy sản phẩm theo id
        Product existingProduct = getProductById(id);
        if (existingProduct != null) {
            // Lấy Category từ categoryId
            Category existingCategory = categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new DataNotFoundException("cannot find category with id : " + productDTO.getCategoryId()));

            // Lấy Brand từ brandId
            Brand existingBrand = brandRepository.findById(productDTO.getBrandId())  // Thêm tìm thương hiệu
                    .orElseThrow(() -> new DataNotFoundException("cannot find brand with id : " + productDTO.getBrandId()));

            // Cập nhật thông tin sản phẩm
            existingProduct.setName(productDTO.getName());
            existingProduct.setCategory(existingCategory);
            existingProduct.setPrice(productDTO.getPrice());
            existingProduct.setDescription(productDTO.getDescription());
            existingProduct.setThumbnail(productDTO.getThumbnail());
            existingProduct.setBrand(existingBrand);  // Cập nhật thương hiệu

            return productRepository.save(existingProduct);
        }
        return null;
    }

    @Override
    public void deleteProduct(long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        optionalProduct.ifPresent(productRepository::delete);
    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    @Transactional
    public ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO) throws DataNotFoundException, InvalidParamExeption {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("cannot find product with id : " + productId));

        ProductImage newProductImage = ProductImage.builder()
                .product(existingProduct)
                .imageUrl(productImageDTO.getImageUrl())
                .build();

        // Không cho insert quá 5 ảnh cho 1 sản phẩm
        int size = productImageReponsitory.findByProductId(productId).size();
        if (size >= ProductImage.MAXIMUM_IMAGES_PER_PRODUCT) {
            throw new InvalidParamExeption("number of images must be <= " + ProductImage.MAXIMUM_IMAGES_PER_PRODUCT);
        }

        return productImageReponsitory.save(newProductImage);
    }

    //tìm sản phẩm theo category
    @Override
    public Page<ProductResponse> getProductsByCategory(Long categoryId, PageRequest pageRequest) {
        // Log thêm để xác minh số lượng sản phẩm
        System.out.println("Fetching products for categoryId: " + categoryId);

        // Gọi repository để lấy sản phẩm theo categoryId
        Page<Product> products = productRepository.findByCategoryId(categoryId, pageRequest);

        // Log số lượng sản phẩm tìm thấy
        System.out.println("Number of products found: " + products.getTotalElements());

        // Chuyển từ Product sang ProductResponse
        return products.map(ProductResponse::fromProduct);
    }



    @Override
    public Page<ProductResponse> searchProductsByName(String keyword, Pageable pageable) {
        // Tách keyword thành nhiều từ
        String[] words = keyword.trim().toLowerCase().split("\\s+");
        Set<Product> uniqueProducts = new LinkedHashSet<>();

        // Duyệt qua từng từ khóa, gọi truy vấn và thêm vào Set để loại trùng
        for (String word : words) {
            Page<Product> partialPage = productRepository.searchByKeyword(word, PageRequest.of(0, 50));
            uniqueProducts.addAll(partialPage.getContent()); // Lấy tất cả sản phẩm có chứa từ khóa
        }

        // Convert Set thành List để phân trang thủ công
        List<Product> filteredList = new ArrayList<>(uniqueProducts);
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), filteredList.size());

        List<ProductResponse> productResponses = filteredList.subList(start, end)
                .stream()
                .map(ProductResponse::fromProduct)
                .collect(Collectors.toList());

        return new PageImpl<>(productResponses, pageable, filteredList.size());
    }





}
