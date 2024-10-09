package com.LaptopWeb.service;

import com.LaptopWeb.dto.request.CategoryRequest;
import com.LaptopWeb.entity.Category;
import com.LaptopWeb.exception.AppException;
import com.LaptopWeb.exception.ErrorApp;
import com.LaptopWeb.mapper.CategoryMapper;
import com.LaptopWeb.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    public Category getCategoryById(Integer categoryId) {
        System.out.println(categoryRepository.findById(categoryId));

        return categoryRepository.findById(categoryId).orElseThrow(() ->
                new AppException(ErrorApp.CATEGORY_NOT_FOUND));
    }

    public Category getByName(String name) {
        return categoryRepository.findByName(name).orElseThrow(() ->
                new AppException(ErrorApp.BRAND_NOT_FOUND));
    }

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    public Category createCategory(CategoryRequest request) {
        Category category = categoryMapper.toCategory(request);

        if(categoryRepository.existsByName(request.getName())) {
            throw new AppException(ErrorApp.CATEGORY_NAME_EXISTED);
        }

        if(request.getParent_id() != null) {
            Category categoryParent = getCategoryById(request.getParent_id());

            category.setParent(categoryParent);
        }

        return categoryRepository.save(category);
    }

    public Category updateCategory(Integer categoryId, CategoryRequest request) {
        Category category = getCategoryById(categoryId);

        if(request.getName().equals(category.getName()) || categoryRepository.existsByName(request.getName())) {
            throw new AppException(ErrorApp.CATEGORY_NAME_EXISTED);
        }

        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setEnabled(request.isEnabled());

        if(request.getParent_id() != null) {
            Category categoryParent = getCategoryById(request.getParent_id());

            category.setParent(categoryParent);
        }

        return  categoryRepository.save(category);
    }

    public List<Category> getCategory() {
        List<Category> categories = getAll();

        List<Category> results = new ArrayList<>();

        for(Category category:categories) {
            if(category.getParent() == null) results.add(category);
        }

        return results;
    }


    public void deleteCategory(Integer categoryId) {
        Category category = getCategoryById(categoryId);

        categoryRepository.deleteById(categoryId);
    }
}
