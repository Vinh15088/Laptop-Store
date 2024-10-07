package com.LaptopWeb.controller;

import com.LaptopWeb.dto.request.BrandRequest;
import com.LaptopWeb.dto.response.ApiResponse;
import com.LaptopWeb.dto.response.BrandResponse;
import com.LaptopWeb.entity.Brand;
import com.LaptopWeb.mapper.BrandMapper;
import com.LaptopWeb.service.BrandService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/brands")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @Autowired
    private BrandMapper brandMapper;

    @PostMapping
    public ResponseEntity<?> createBrand(
            @Valid
            @RequestPart("brand") BrandRequest request,
            @RequestPart("logo") MultipartFile logo
    ) throws Exception {

        Brand brand = brandService.createBrand(request, logo);

        BrandResponse brandResponse = brandMapper.toBrandResponse(brand);

        ApiResponse apiResponse = ApiResponse.builder()
                .success(true)
                .content(brandResponse)
                .build();

        return ResponseEntity.ok().body(apiResponse);
    }

    @GetMapping("/{brandId}")
    public ResponseEntity<ApiResponse<?>> getBrandById(@PathVariable("brandId") Integer brandId) {
        Brand brand = brandService.getById(brandId);

        BrandResponse brandResponse = brandMapper.toBrandResponse(brand);

        ApiResponse apiResponse = ApiResponse.builder()
                .success(true)
                .content(brandResponse)
                .build();

        return ResponseEntity.ok().body(apiResponse);
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<?>> getAllBrand() {
        List<Brand> brands = brandService.getAllBrand();

        List<BrandResponse> listBrandResponse = brands.stream().map(brandMapper::toBrandResponse).toList();

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .success(true)
                .content(listBrandResponse)
                .build();

        return ResponseEntity.ok().body(apiResponse);
    }


    @PutMapping("/{brandId}")
    public ResponseEntity<ApiResponse<?>> updateBrand(
            @Valid
            @PathVariable("brandId") Integer brandId,
            @RequestPart("brand") BrandRequest request,
            @RequestPart("logo") MultipartFile logo) throws Exception {

        Brand brand = brandService.updateBrand(brandId, request, logo);

        BrandResponse brandResponse = brandMapper.toBrandResponse(brand);

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .success(true)
                .content(brandResponse)
                .build();

        return ResponseEntity.ok().body(apiResponse);
    }

    @DeleteMapping("/{brandId}")
    public ResponseEntity<ApiResponse<?>> deleteBrand (@PathVariable("brandId") Integer brandId) throws Exception {
        brandService.deleteBrand(brandId);

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .success(true)
                .content("Delete Brand Successfully")
                .build();

        return ResponseEntity.ok().body(apiResponse);

    }
}
