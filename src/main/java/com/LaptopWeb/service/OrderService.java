package com.LaptopWeb.service;

import com.LaptopWeb.dto.request.OrderRequest;
import com.LaptopWeb.entity.Order;
import com.LaptopWeb.entity.StatusOrder;
import com.LaptopWeb.entity.User;
import com.LaptopWeb.exception.AppException;
import com.LaptopWeb.exception.ErrorApp;
import com.LaptopWeb.mapper.OrderMapper;
import com.LaptopWeb.repository.OrderRepository;
import com.LaptopWeb.repository.StatusOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private StatusOrderRepository statusOrderRepository;


    public Order createOrder(OrderRequest request) {
        Order order = orderMapper.toOrder(request);

        if(request.getUserId() != null) {
            User user = userService.getById(request.getUserId());

            order.setUser(user);
        }

        if(request.getStatusOrderId() != null) {
            StatusOrder statusOrder = (StatusOrder) statusOrderRepository.findById(request.getStatusOrderId()).orElseThrow(() ->
                    new AppException(ErrorApp.STATUS_ORDER_NOT_FOUND));

            order.setStatusOrder(statusOrder);
        }

        order.setCreatedAt(new Date());

        return orderRepository.save(order);
    }

    public Order getOrderById(Integer id) {
        return orderRepository.findById(id).orElseThrow(() ->
                new AppException(ErrorApp.ORDER_NOT_FOUND));
    }

    public Order getOrderByOrderCode(String orderCode) {
        return orderRepository.findByOrderCode(orderCode).orElseThrow(() ->
                new AppException(ErrorApp.ORDER_NOT_FOUND));
    }

    public Page<Order> getAllOrder(Integer number, Integer size, String sortBy, String order) {
        Sort sort = Sort.by(Sort.Direction.valueOf(order.toUpperCase()), sortBy);

        Pageable pageable = PageRequest.of(number, size, sort);

        return orderRepository.findAll(pageable);
    }

    public Order updateOrder(Integer id, OrderRequest request) {
        Order order = getOrderById(id);

        Order order1 = orderMapper.toOrder(request);

        if(request.getUserId() != null) {
            User user = userService.getById(request.getUserId());

            order1.setUser(user);
        }

        if(request.getStatusOrderId() != null) {
            StatusOrder statusOrder = (StatusOrder) statusOrderRepository.findById(request.getStatusOrderId()).orElseThrow(() ->
                    new AppException(ErrorApp.STATUS_ORDER_NOT_FOUND));

            order1.setStatusOrder(statusOrder);
        }

        order1.setId(order.getId());
        order1.setCreatedAt(order.getCreatedAt());

        return orderRepository.save(order1);
    }

    public void deleteOrder(Integer id) {
        Order order = getOrderById(id);

        orderRepository.deleteById(id);
    }


}
