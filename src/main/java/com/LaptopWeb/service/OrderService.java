package com.LaptopWeb.service;

import com.LaptopWeb.dto.request.OrderDetailRequest;
import com.LaptopWeb.dto.request.OrderRequest;
import com.LaptopWeb.entity.*;
import com.LaptopWeb.exception.AppException;
import com.LaptopWeb.exception.ErrorApp;
import com.LaptopWeb.mapper.OrderDetailMapper;
import com.LaptopWeb.mapper.OrderMapper;
import com.LaptopWeb.repository.OrderDetailRepository;
import com.LaptopWeb.repository.OrderRepository;
import com.LaptopWeb.repository.OrderStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@EnableMethodSecurity
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderStatusRepository orderStatusRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;


    @PreAuthorize("hasRole('USER')")
    public Order createOrder(OrderRequest request, Integer userId) {
        OrderStatus orderStatus = orderStatusRepository.findById(1).orElseThrow(); // get status PENDING

        User user = userService.getById(userId);

        List<OrderDetailRequest>  orderDetailRequestList = request.getDetailRequests();

        Order order = orderMapper.toOrder(request);

        List<OrderDetail> orderDetails = orderDetailRequestList.stream().map(
                detailRequest -> {
                    Product product = productService.getProductById(detailRequest.getProductId());

                    OrderDetail orderDetail = orderDetailMapper.toOrderDetail(detailRequest);
                    orderDetail.setOrder(order);
                    orderDetail.setProduct(product);
                    orderDetail.setTotalPrice(detailRequest.getUnitPrice() * detailRequest.getQuantity());

                    return orderDetail;
                }
        ).toList();

        order.setOrderStatus(orderStatus);
        order.setUser(user);
        order.setOrderDetails(orderDetails);

        return orderRepository.save(order);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Order getOrderById(Integer id) {
        return orderRepository.findById(id).orElseThrow(() ->
                new AppException(ErrorApp.ORDER_NOT_FOUND));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Order getOrderByOrderCode(String orderCode) {
        return orderRepository.findByOrderCode(orderCode).orElseThrow(() ->
                new AppException(ErrorApp.ORDER_NOT_FOUND));
    }

    @PreAuthorize("#username == principal.claims['data']['username']")
    public List<Order> getMyOrder(String username) {
        return orderRepository.getAllOrderByUser(username);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Page<Order> getAllOrders(Integer number, Integer size, String sortBy, String order) {
        Sort sort = Sort.by(Sort.Direction.valueOf(order.toUpperCase()), sortBy);

        Pageable pageable = PageRequest.of(number, size, sort);

        return orderRepository.findAll(pageable);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Order updateStatus(String orderStatus, String orderCode) {
        OrderStatus orderStatus1 = orderStatusRepository.findByName(orderStatus).orElseThrow();

        int update = orderRepository.updateOrderStatus(orderStatus1, orderCode);

        if(update == 1) return getOrderByOrderCode(orderCode);
        else throw new AppException(ErrorApp.UPDATE_ORDER_STATUS_FAIL);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteOrder(Integer id) {
        Order order = getOrderById(id);

        orderRepository.deleteById(id);
    }


}
