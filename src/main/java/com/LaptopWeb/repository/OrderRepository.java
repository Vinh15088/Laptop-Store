package com.LaptopWeb.repository;

import com.LaptopWeb.entity.Order;
import com.LaptopWeb.entity.OrderStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    Optional<Order> findByOrderCode(String orderCode);

    Optional<Order> findByTransactionId(String transactionId);

    boolean existsByOrderCode(String orderCode);

    boolean existsByTransactionId(String transactionId);

    @Query("SELECT COUNT(o) > 0 FROM  Order o WHERE o.orderCode = ?1 AND o.transactionId = ?2")
    boolean existsByOrderCodeAndTransactionId(String orderCode, String transactionId);

    @Transactional
    @Modifying
    @Query("UPDATE Order o SET o.orderStatus = ?1 WHERE o.orderCode = ?2")
    int updateOrderStatus(OrderStatus orderStatus, String orderCode);

    @Query("SELECT o FROM Order o WHERE o.user.username = ?1")
    List<Order> getAllOrderByUser(String username);


}
