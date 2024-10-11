package com.LaptopWeb.controller;

import com.LaptopWeb.dto.request.ProductRequest;
import com.LaptopWeb.dto.response.ApiResponse;
import com.LaptopWeb.dto.response.ProductResponse;
import com.LaptopWeb.entity.Product;
import com.LaptopWeb.mapper.ProductMapper;
import com.LaptopWeb.service.ProductService;
import com.LaptopWeb.utils.PageInfo;
import com.amazonaws.services.machinelearning.model.PredictRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductMapper productMapper;

    private static final String PAGE_SIZE = "20";
    private static final String PAGE_NUMBER = "1";

    @PostMapping
    public ResponseEntity<?> createProduct(
            @Valid
            @RequestPart("product") ProductRequest request,
            @RequestPart("image")MultipartFile image
            ) throws Exception {
        Product product = productService.createProduct(request, image);

        ProductResponse productResponse = productMapper.toProductResponse(product);

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .success(true)
                .content(productResponse)
                .build();

        return ResponseEntity.ok().body(apiResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") Integer id) {
        Product product = productService.getProductById(id);

        ProductResponse productResponse = productMapper.toProductResponse(product);

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .success(true)
                .content(productResponse)
                .build();

        return ResponseEntity.ok().body(apiResponse);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllProduct(
        @RequestParam(name = "size", defaultValue = PAGE_SIZE) Integer size,
        @RequestParam(name = "number", defaultValue = PAGE_NUMBER) Integer number,
        @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
        @RequestParam(name = "order", defaultValue = "asc") String order
    ) {
        Page<Product> productPage = productService.getAllProduct(number-1, size, sortBy, order);

        List<Product> products = productPage.getContent();
        List<ProductResponse> productResponses = products.stream().map(productMapper::toProductResponse).toList();

        PageInfo pageInfo = PageInfo.builder()
                .page(productPage.getNumber()+1)
                .size(productPage.getSize())
                .totalElements(productPage.getNumberOfElements())
                .totalPages(productPage.getTotalPages())
                .build();

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .success(true)
                .content(productResponses)
                .pageInfo(pageInfo)
                .build();

        return ResponseEntity.ok().body(apiResponse);
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<?> getAllProductByCategory(
            @PathVariable("id") Integer id,
            @RequestParam(name = "size", defaultValue = PAGE_SIZE) Integer size,
            @RequestParam(name = "number", defaultValue = PAGE_NUMBER) Integer number,
            @RequestParam(name = "minPrice", defaultValue = "0") Long minPrice,
            @RequestParam(name = "maxPrice", defaultValue = "100000000") Long maxPrice
    ) {
        Page<Product> productPage = productService.getAllProductByCategory(id, number-1, size, minPrice, maxPrice);

        List<Product> products = productPage.getContent();
        List<ProductResponse> productResponses = products.stream().map(productMapper::toProductResponse).toList();

        PageInfo pageInfo = PageInfo.builder()
                .page(productPage.getNumber()+1)
                .size(productPage.getSize())
                .totalElements(productPage.getNumberOfElements())
                .totalPages(productPage.getTotalPages())
                .build();

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .success(true)
                .content(productResponses)
                .pageInfo(pageInfo)
                .build();

        return ResponseEntity.ok().body(apiResponse);
    }

    @GetMapping("/brand/{id}")
    public ResponseEntity<?> getAllProductByBrand(
            @PathVariable("id") Integer id,
            @RequestParam(name = "size", defaultValue = PAGE_SIZE) Integer size,
            @RequestParam(name = "number", defaultValue = PAGE_NUMBER) Integer number,
            @RequestParam(name = "minPrice", defaultValue = "0") Long minPrice,
            @RequestParam(name = "maxPrice", defaultValue = "100000000") Long maxPrice
    ) {
        Page<Product> productPage = productService.getAllProductByBrand(id, number-1, size, minPrice, maxPrice);

        List<Product> products = productPage.getContent();
        List<ProductResponse> productResponses = products.stream().map(productMapper::toProductResponse).toList();

        PageInfo pageInfo = PageInfo.builder()
                .page(productPage.getNumber()+1)
                .size(productPage.getSize())
                .totalElements(productPage.getNumberOfElements())
                .totalPages(productPage.getTotalPages())
                .build();

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .success(true)
                .content(productResponses)
                .pageInfo(pageInfo)
                .build();

        return ResponseEntity.ok().body(apiResponse);
    }

    @GetMapping("/search")
    public ResponseEntity<?> getProductWithKeyword(
            @RequestParam(name = "size", defaultValue = PAGE_SIZE) Integer size,
            @RequestParam(name = "number", defaultValue = PAGE_NUMBER) Integer number,
            @RequestParam(name = "keyword", required = false) String keyword

    ) {
        Page<Product> productPage = productService.getProductWithKeyword(number-1, size, keyword);

        List<Product> products = productPage.getContent();
        List<ProductResponse> productResponses = products.stream().map(productMapper::toProductResponse).toList();

        PageInfo pageInfo = PageInfo.builder()
                .page(productPage.getNumber()+1)
                .size(productPage.getSize())
                .totalElements(productPage.getNumberOfElements())
                .totalPages(productPage.getTotalPages())
                .build();

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .success(true)
                .content(productResponses)
                .pageInfo(pageInfo)
                .build();

        return ResponseEntity.ok().body(apiResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable("id") Integer id,
            @RequestPart(name = "product", required = false) ProductRequest request,
            @RequestPart(name = "image", required = false) MultipartFile image
    ) throws Exception {
        Product product = productService.updateProduct(id, request, image);

        ProductResponse productResponse = productMapper.toProductResponse(product);

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .success(true)
                .content(productResponse)
                .build();

        return ResponseEntity.ok().body(apiResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Integer id) throws Exception {
        productService.deleteProduct(id);

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .success(true)
                .content("Delete product successful")
                .build();

        return ResponseEntity.ok().body(apiResponse);
    }

}
