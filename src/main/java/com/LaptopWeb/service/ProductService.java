package com.LaptopWeb.service;

import com.LaptopWeb.dto.request.ProductRequest;
import com.LaptopWeb.entity.Brand;
import com.LaptopWeb.entity.Category;
import com.LaptopWeb.entity.Product;
import com.LaptopWeb.exception.AppException;
import com.LaptopWeb.exception.ErrorApp;
import com.LaptopWeb.mapper.ProductMapper;
import com.LaptopWeb.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private AwsS3Service awsS3Service;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

    public Product createProduct(ProductRequest request, MultipartFile imageProduct) throws Exception {
        if (productRepository.existsByName(request.getName())) {
            throw  new AppException(ErrorApp.PRODUCT_NAME_EXISTED);
        }

        Product product = productMapper.toProduct(request);

        if(request.getCategory_id() != null) {
            Category category = categoryService.getCategoryById(request.getCategory_id());

            product.setCategory(category);
        }

        if(request.getBrand_id() != null) {
            Brand brand = brandService.getById(request.getBrand_id());

            product.setBrand(brand);
        }

        product.setCreatedAt(new Date());
        product.setUpdatedAt(null);
        product.setImage(imageProduct.getOriginalFilename());

        Product product1 = productRepository.save(product);

        String folder = "products/" + product1.getId();
        awsS3Service.saveImageToS3(imageProduct, folder);

        return product1;
    }

    public Product getProductById(Integer id) {
        return productRepository.findById(id).orElseThrow(() ->
                new AppException(ErrorApp.PRODUCT_NOT_FOUND));
    }

    public Page<Product> getAllProduct(Integer number, Integer size, String sortBy, String order) {
        Sort sort = Sort.by(Sort.Direction.valueOf(order.toUpperCase()), sortBy);

        Pageable pageable = PageRequest.of(number, size, sort);

        return productRepository.findAll(pageable);
    }

    public Page<Product> getAllProductByCategory(Integer categoryId, Integer number, Integer size, Long minPrice, Long maxPrice) {
        Category category = categoryService.getCategoryById(categoryId);

        Pageable pageable = PageRequest.of(number, size);

        return productRepository.findAllWithCategoryId(categoryId, minPrice, maxPrice, pageable);
    }

    public Page<Product> getAllProductByBrand(Integer brandId, Integer number, Integer size, Long minPrice, Long maxPrice) {
        Brand brand = brandService.getById(brandId);

        Pageable pageable = PageRequest.of(number, size);

        return productRepository.findAllWithBrandId(brandId, minPrice, maxPrice, pageable);
    }

    public Page<Product> getProductWithKeyword(Integer number, Integer size, String keyword) {
        Pageable pageable = PageRequest.of(number, size);

        return (keyword != null && !keyword.isEmpty())
                ? productRepository.findAll(keyword, pageable)
                : productRepository.findAll(pageable);
    }

    public List<Product> getProductsByIds(List<Integer> productIds) {
        return productRepository.findAllById(productIds);
    }

    public Product updateProduct(Integer id, ProductRequest request, MultipartFile imageProduct) throws Exception {
        Product product = getProductById(id);

        if(!request.getName().equals(product.getName()) &&
            productRepository.existsByName(request.getName())) {
            throw new AppException(ErrorApp.PRODUCT_NAME_EXISTED);
        }

        Product product1 = productMapper.toProduct(request);

        if(request.getCategory_id() != null) {
            Category category = categoryService.getCategoryById(request.getCategory_id());

            product1.setCategory(category);
        }

        if(request.getBrand_id() != null) {
            Brand brand = brandService.getById(request.getBrand_id());

            product1.setBrand(brand);
        }

        product1.setId(product.getId());
        product1.setCreatedAt(product.getCreatedAt());
        product1.setUpdatedAt(new Date());

        if(imageProduct != null) {
            String folder = "products/" + id;

//            if(product.getImage() != null) awsS3Service.deleteImageFromS3(folder, product.getImage());
//            awsS3Service.updateImageInS3(imageProduct, folder, product.getImage());

            awsS3Service.updateImageInS3(imageProduct, folder, product.getImage());

            product1.setImage(imageProduct.getOriginalFilename());
        }

        return productRepository.save(product1);
    }


    public void deleteProduct(Integer id) throws Exception {
        Product product = getProductById(id);

        String folder = "products/" + id;

        awsS3Service.deleteImageFromS3(folder, product.getImage());

        productRepository.deleteById(id);
    }




}
