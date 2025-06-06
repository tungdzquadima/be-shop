package com.example.shopapp.services;

import com.example.shopapp.models.Brand;

import java.util.List;
import java.util.Optional;

public interface IBrandService {

    // Tạo mới thương hiệu
    Brand createBrand(Brand brand);

    // Cập nhật thông tin thương hiệu
    Brand updateBrand(Long id, Brand brand);

    // Lấy tất cả thương hiệu
    List<Brand> getAllBrands();

    // Lấy thương hiệu theo ID
    Optional<Brand> getBrandById(Long id);

    // Xóa thương hiệu theo ID
    void deleteBrand(Long id);

    // Tìm kiếm thương hiệu theo tên
    Optional<Brand> getBrandByName(String name);
}
