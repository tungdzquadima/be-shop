package com.example.shopapp.services;

import com.example.shopapp.dtos.CategoryDTO;
import com.example.shopapp.models.Category;

import java.util.List;

public interface ICategoryService {
    Category createCategory(CategoryDTO category);// khởi tạo
    Category getCategoryById(long id);
    List<Category> getAllCategories();// lấy ra tất cả category
    Category updateCategory(long categoryid, CategoryDTO category);
    void deleteCategory(long id);

}
