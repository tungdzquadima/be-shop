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
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
                .brand(existingBrand)  // Thiết lập thương hiệu cho sản phẩm
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
}
