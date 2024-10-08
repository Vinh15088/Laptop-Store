package com.LaptopWeb.repository;

import com.LaptopWeb.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    boolean existsByName(String name);


    @Query("SELECT p FROM Product p WHERE p.name LIKE %?1% " +
            "OR p.category.name LIKE %?1% " +
            "OR p.brand.name LIKE %?1%")
    public Page<Product> findAll(String keyword, Pageable pageable);


    @Query("SELECT p FROM Product p WHERE p.category.id = ?1 " +
            "AND p.price >= ?2 AND p.price <= ?3")
    public Page<Product> findAllWithCategoryId(Integer id, Long minPrice, Long maxPrice, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.brand.id = ?1 " +
            "AND p.price >= ?2 AND p.price <= ?3")
    public Page<Product> findAllWithBrandId(Integer id, Long minPrice, Long maxPrice, Pageable pageable);

}

