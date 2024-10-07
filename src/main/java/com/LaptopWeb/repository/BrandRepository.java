package com.LaptopWeb.repository;

import com.LaptopWeb.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer> {
    boolean existsByName(String name);

    Optional<Brand> findByName(String name);
}
