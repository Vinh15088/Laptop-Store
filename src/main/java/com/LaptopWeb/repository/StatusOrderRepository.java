package com.LaptopWeb.repository;

import com.LaptopWeb.entity.StatusOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusOrderRepository extends JpaRepository<StatusOrder, Integer> {
}
