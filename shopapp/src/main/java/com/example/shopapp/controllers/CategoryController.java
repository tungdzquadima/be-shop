package com.example.shopapp.controllers;

import com.example.shopapp.dtos.CategoryDTO;
import com.example.shopapp.models.Category;
import com.example.shopapp.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/categories")
public class CategoryController {
    private final CategoryService categoryService;
    // public bên dưới tương đương với @RequiredArgsConstructor nhưng dùng @RequiredArgsConstructor không dược
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    @PostMapping("")
    public ResponseEntity<?> createCategories(@Valid @RequestBody CategoryDTO categoryDTO,
                                              BindingResult result){
        if (result.hasErrors()){
            List<String> errMesage=result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errMesage);
        }
        categoryService.createCategory(categoryDTO);
        return ResponseEntity.ok("Insert category successfully" );
    }

    // hiển thị tất cả các categories
    @GetMapping("") // /?page=1&limit=10
    public ResponseEntity<List<Category>> getAllCategories(
            @RequestParam("page") int page,// nếu thiếu 1 tham số là fail
            @RequestParam("limit") int limit
    ){
        List<Category> categories= categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }


    @PutMapping("/{id}")
    public ResponseEntity<String> updateAllCategories(@PathVariable long id,@Valid @RequestBody CategoryDTO categoryDTO){
        categoryService.updateCategory(id,categoryDTO);
        return ResponseEntity.ok("Update category successfully");
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAllCategories(@PathVariable long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("delete category successfully with id: "+id);
    }

}
