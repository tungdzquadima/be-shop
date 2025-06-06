package com.example.shopapp.controllers;

import com.example.shopapp.models.Brand;
import com.example.shopapp.services.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/brands")
public class BrandController {

    @Autowired
    private BrandService brandService;

    // Tạo mới thương hiệu
    @PostMapping
    public ResponseEntity<Brand> createBrand(@RequestBody Brand brand) {
        Brand createdBrand = brandService.createBrand(brand);
        return new ResponseEntity<>(createdBrand, HttpStatus.CREATED);
    }

    // Cập nhật thông tin thương hiệu theo ID
    @PutMapping("/{id}")
    public ResponseEntity<Brand> updateBrand(@PathVariable Long id, @RequestBody Brand brand) {
        try {
            Brand updatedBrand = brandService.updateBrand(id, brand);
            return new ResponseEntity<>(updatedBrand, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);  // Trả về 404 nếu không tìm thấy thương hiệu
        }
    }

    // Lấy tất cả thương hiệu
    @GetMapping
    public ResponseEntity<List<Brand>> getAllBrands() {
        List<Brand> brands = brandService.getAllBrands();
        return new ResponseEntity<>(brands, HttpStatus.OK);
    }

    // Lấy thương hiệu theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Brand> getBrandById(@PathVariable Long id) {
        Optional<Brand> brand = brandService.getBrandById(id);
        return brand.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));  // Trả về 404 nếu không tìm thấy
    }

    // Xóa thương hiệu theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBrand(@PathVariable Long id) {
        try {
            brandService.deleteBrand(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);  // Trả về 204 nếu xóa thành công
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);  // Trả về 404 nếu không tìm thấy thương hiệu
        }
    }

    // Tìm thương hiệu theo tên
    @GetMapping("/name/{name}")
    public ResponseEntity<Brand> getBrandByName(@PathVariable String name) {
        Optional<Brand> brand = brandService.getBrandByName(name);
        return brand.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));  // Trả về 404 nếu không tìm thấy
    }
}
