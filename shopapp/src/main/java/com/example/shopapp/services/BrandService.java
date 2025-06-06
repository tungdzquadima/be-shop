package com.example.shopapp.services;

 // Thêm dòng này ở đầu tệp

import com.example.shopapp.models.Brand;
import com.example.shopapp.repositories.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BrandService implements IBrandService {

    @Autowired
    private BrandRepository brandRepository;

    @Override
    public Brand createBrand(Brand brand) {
        return brandRepository.save(brand);
    }

    @Override
    public Brand updateBrand(Long id, Brand brand) {
        if (brandRepository.existsById(id)) {
            brand.setId(id);
            return brandRepository.save(brand);
        }
        throw new RuntimeException("Brand not found");
    }

    @Override
    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    @Override
    public Optional<Brand> getBrandById(Long id) {
        return brandRepository.findById(id);
    }

    @Override
    public void deleteBrand(Long id) {
        if (brandRepository.existsById(id)) {
            brandRepository.deleteById(id);
        } else {
            throw new RuntimeException("Brand not found");
        }
    }

    @Override
    public Optional<Brand> getBrandByName(String name) {
        return brandRepository.findByName(name);
    }
}

