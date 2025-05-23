package com.example.shopapp.services;
import com.example.shopapp.dtos.CategoryDTO;
import com.example.shopapp.models.Category;
import com.example.shopapp.repositories.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
@Service
//@RequiredArgsConstructor // bạn có thể bật nếu muốn dùng constructor injection tự động
public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category createCategory(CategoryDTO categoryDTO) {
        Category newCategory = Category.builder()
                .name(categoryDTO.getName())
                .build();
        return categoryRepository.save(newCategory);
    }
//@Override
//public Category createCategory(CategoryDTO categoryDTO) {
//    Category newCategory = new Category();
//    newCategory.setName(categoryDTO.getName());
//    return categoryRepository.save(newCategory);
//}

    @Override
    public Category getCategoryById(long id) {
        return categoryRepository.findById(id).orElseThrow(()->new RuntimeException("Category canot found"));
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category updateCategory(long categoryid, CategoryDTO categoryDTO) {
        Category existingCategory=getCategoryById(categoryid);
        existingCategory.setName(categoryDTO.getName());
        categoryRepository.save(existingCategory);
        return  existingCategory;
    }

    @Override
    public void deleteCategory(long id) {
        categoryRepository.deleteById(id);
    }}
//    // các method còn lại ...
//}
//
////@RequiredArgsConstructor
////public class CategoryService implements ICategoryService{
////    private final CategoryRepository categoryRepository;
////
////    public CategoryService(CategoryRepository categoryRepository) {
////        this.categoryRepository = categoryRepository;
////    }
////    @Override
////    public Category createCategory(CategoryDTO categoryDTO) {
////        Category newCategory=Category.builder().name(categoryDTO.getName()).build();
////        return categoryRepository.save(newCategory);
////    }
////
////    @Override
////    public Category getCategoryById(long id) {
////        return categoryRepository.findById(id).orElseThrow(()->new RuntimeException("category not found"));
////    }
////
////    @Override
////    public List<Category> getAllCategories() {
////        return categoryRepository.findAll();
////    }
////
////    @Override
////    public Category updateCategory(long categoryid, CategoryDTO categoryDTO) {
////        Category exitingCategory=getCategoryById(categoryid);
////        exitingCategory.setName(categoryDTO.getName());
////        return exitingCategory;
////    }
////
////    @Override
////    public void deleteCategory(long id) {
////        // xóa cứng
////         categoryRepository.deleteById(id);
////    }
////}
