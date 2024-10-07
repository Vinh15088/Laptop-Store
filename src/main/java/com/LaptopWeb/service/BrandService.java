package com.LaptopWeb.service;

import com.LaptopWeb.dto.request.BrandRequest;
import com.LaptopWeb.entity.Brand;
import com.LaptopWeb.exception.AppException;
import com.LaptopWeb.exception.ErrorApp;
import com.LaptopWeb.mapper.BrandMapper;
import com.LaptopWeb.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class BrandService {

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private AwsS3Service awsS3Service;

//    @Autowired
//    private AwsService awsService;

    @Autowired
    private BrandMapper brandMapper;

    public Brand createBrand(BrandRequest request, MultipartFile logo) throws Exception {
        if(brandRepository.existsByName(request.getName())) throw new AppException(ErrorApp.BRAND_NAME_EXISTED);

        Brand brand = Brand.builder()
                .name(request.getName())
                .logo(logo.getOriginalFilename())
                .build();

        Brand brand1 = brandRepository.save(brand);

        String folder = "brands/" + brand1.getId();

        awsS3Service.saveImageToS3(logo, folder);

        return brand1;
    }

    public List<Brand> getAllBrand() {
        return brandRepository.findAll();
    }

    public Brand getById(Integer brandId) {
        Brand brand = brandRepository.findById(brandId).orElseThrow(() ->
                new AppException(ErrorApp.BRAND_NOT_FOUND));

        return brand;
    }

    public Brand getByName(String name) {
        Brand brand = brandRepository.findByName(name).orElseThrow(() ->
                new AppException(ErrorApp.BRAND_NOT_FOUND));

        return brand;
    }


    public Brand updateBrand(Integer brandId, BrandRequest request, MultipartFile logo) throws Exception {
        Brand brand = getById(brandId);

        brand.setName(request.getName());

        String folder = "brands/" + brandId;

        if(logo != null) {
            awsS3Service.deleteImageFromS3(folder, logo.getOriginalFilename());

            brand.setLogo(logo.getOriginalFilename());

            awsS3Service.saveImageToS3(logo, folder);
        }

        return brandRepository.save(brand);
    }

    public void deleteBrand(Integer brandId) throws Exception {
        Brand brand = getById(brandId);

        String folder = "brands/" + brandId;

        awsS3Service.deleteImageFromS3(folder, brand.getLogo());

        brandRepository.deleteById(brandId);
    }


}
