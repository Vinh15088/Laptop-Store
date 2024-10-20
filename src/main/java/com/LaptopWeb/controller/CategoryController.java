package com.LaptopWeb.controller;

import com.LaptopWeb.dto.request.CategoryRequest;
import com.LaptopWeb.dto.response.ApiResponse;
import com.LaptopWeb.dto.response.CategoryResponse;
import com.LaptopWeb.entity.Category;
import com.LaptopWeb.mapper.CategoryMapper;
import com.LaptopWeb.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryMapper categoryMapper;

    @PostMapping /*checked success*/
    public ResponseEntity<ApiResponse<?>> createCategory(@Valid @RequestBody CategoryRequest request) {
        Category category = categoryService.createCategory(request);

        CategoryResponse categoryResponse = categoryMapper.toCategoryResponse(category);

        ApiResponse apiResponse = ApiResponse.builder()
                .success(true)
                .content(categoryResponse)
                .build();

        return ResponseEntity.ok().body(apiResponse);
    }

    @GetMapping /*checked success*/
    public ResponseEntity<?> getCategory(@RequestParam(name="name", required = false) String name) {
        if(name != null && !name.isEmpty()) {
            Category category = categoryService.getByName(name);

            CategoryResponse categoryResponse = categoryMapper.toCategoryResponse(category);

            ApiResponse apiResponse = ApiResponse.builder()
                    .success(true)
                    .content(categoryResponse)
                    .build();

            return ResponseEntity.ok().body(apiResponse);
        } else {
            List<Category> categories = categoryService.getCategory();

            List<CategoryResponse> categoryResponses = categories.stream().map(categoryMapper::toCategoryResponse).toList();

            ApiResponse apiResponse = ApiResponse.builder()
                    .success(true)
                    .content(categoryResponses)
                    .build();

            return ResponseEntity.ok().body(apiResponse);
        }
    }

    @GetMapping("/{categoryId}") /*checked success*/
    public ResponseEntity<ApiResponse<?>> getById(@PathVariable("categoryId") Integer categoryId) {
        Category category = categoryService.getCategoryById(categoryId);

        CategoryResponse categoryResponse = categoryMapper.toCategoryResponse(category);

        ApiResponse apiResponse = ApiResponse.builder()
                .success(true)
                .content(categoryResponse)
                .build();

        return ResponseEntity.ok().body(apiResponse);
    }

    @GetMapping("/all") /*checked success*/
    public ResponseEntity<ApiResponse<?>> getAll() {
        List<Category> categories = categoryService.getAll();

        List<CategoryResponse> listCategories = categories.stream().map(categoryMapper::toCategoryResponse).toList();

        ApiResponse apiResponse = ApiResponse.builder()
                .success(true)
                .content(listCategories)
                .build();

        return ResponseEntity.ok().body(apiResponse);
    }

    @PutMapping("/{categoryId}") /*checked success*/
    public ResponseEntity<ApiResponse<?>> updateCategory(
            @PathVariable("categoryId") Integer categoryId,
            @RequestBody CategoryRequest request
    ) {
        Category category = categoryService.updateCategory(categoryId, request);

        CategoryResponse categoryResponse = categoryMapper.toCategoryResponse(category);

        ApiResponse apiResponse = ApiResponse.builder()
                .success(true)
                .content(categoryResponse)
                .build();

        return ResponseEntity.ok().body(apiResponse);
    }

    @DeleteMapping("/{categoryId}") /*checked success*/
    public ResponseEntity<ApiResponse<?>> deleteCategory(@PathVariable("categoryId") Integer categoryId) {
        categoryService.deleteCategory(categoryId);

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .success(true)
                .content("Delete Category Successfully")
                .build();

        return ResponseEntity.ok().body(apiResponse);
    }

}
