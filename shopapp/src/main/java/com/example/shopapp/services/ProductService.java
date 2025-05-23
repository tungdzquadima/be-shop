package com.example.shopapp.services;

import com.example.shopapp.dtos.ProductDTO;
import com.example.shopapp.dtos.ProductImageDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.exceptions.InvalidParamExeption;
import com.example.shopapp.models.Category;
import com.example.shopapp.models.Product;
import com.example.shopapp.models.ProductImage;
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
public class ProductService implements IProductService{
    private final ProductRepository productRepository;
    private  final CategoryRepository categoryRepository;
    private final ProductImageReponsitory productImageReponsitory;
    @Override
    public Product createProduct(ProductDTO productDTO) throws DataNotFoundException {

            Category existingCategory = categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new DataNotFoundException
                            ("cannot find category witth id : " + productDTO.getCategoryId()));

            Product newProduct = Product.builder()
                    .name(productDTO.getName())
                    .price(productDTO.getPrice())
                    .thumbnail(productDTO.getThumbnail())
                    .description(productDTO.getDescription())
                    .category(existingCategory)
                    .build();
            return productRepository.save(newProduct);

    }

    @Override
    public Product getProductById(long productId) throws Exception {
        return productRepository.findById(productId)
                .orElseThrow(()->new DataNotFoundException("canot find product with id: "+productId));
    }

    @Override
    public Page<ProductResponse> getAllProducts(PageRequest pageRequest) {
        // lấy danh sách sản phẩm thei page và limit
        return productRepository.findAll(pageRequest).map(ProductResponse::fromProduct);
    }

    @Override
    public Product updateProduct(long id, ProductDTO productDTO) throws Exception {
            Product x=getProductById(id);
            if(x!=null){
                // copy các thuộc tính từ dto sang product
                // có thể sử dụng mođelMapper
                Category existingCategory = categoryRepository.findById(productDTO.getCategoryId())
                        .orElseThrow(() -> new DataNotFoundException
                                ("cannot find category witth id : " + productDTO.getCategoryId()));
                x.setName(productDTO.getName());
                x.setCategory(existingCategory);
                x.setPrice(productDTO.getPrice());
                x.setDescription(productDTO.getDescription());
                x.setThumbnail(productDTO.getThumbnail());
                return productRepository.save(x);

            }
            return null;
    }

    @Override
    public void deleteProduct(long id) {
        Optional<Product> optionalProduct=productRepository.findById(id);
        optionalProduct.ifPresent(productRepository::delete);
    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    @Transactional // chatgpt bảo thm
    public ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO) throws DataNotFoundException, InvalidParamExeption {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException
                        ("cannot find product witth id : " ));
        ProductImage x=ProductImage.builder()
                .product(existingProduct)
                .imageUrl(productImageDTO.getImageUrl())
                .build();
        // không cho insert quá 5 ảnh cho 1 sản phẩm
        int size=productImageReponsitory.findByProductId(productId).size();
        if(size>=ProductImage.MAXIMUM_IMAGES_PER_PRODUCT){
            throw new InvalidParamExeption("numberof image must be <= "+ProductImage.MAXIMUM_IMAGES_PER_PRODUCT);
        }
      return  productImageReponsitory.save(x);
    }
}
