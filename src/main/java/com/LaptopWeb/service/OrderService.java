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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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


    public Order createOrder(OrderRequest request, String username) {
        OrderStatus orderStatus = orderStatusRepository.findById(1).orElseThrow(); // get status PENDING

        User user = userService.getByUsername(username);

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

    public Order getOrderById(Integer id) {
        return orderRepository.findById(id).orElseThrow(() ->
                new AppException(ErrorApp.ORDER_NOT_FOUND));
    }

    public Order getOrderByOrderCode(String orderCode) {
        return orderRepository.findByOrderCode(orderCode).orElseThrow(() ->
                new AppException(ErrorApp.ORDER_NOT_FOUND));
    }

    public List<Order> getMyOrder(String username) {
        return orderRepository.getAllOrderByUser(username);
    }

    public Order updateStatus(String orderStatus, String orderCode) {
        OrderStatus orderStatus1 = orderStatusRepository.findByName(orderStatus).orElseThrow();

        int update = orderRepository.updateOrderStatus(orderStatus1, orderCode);

        if(update == 1) return getOrderByOrderCode(orderCode);
        else throw new AppException(ErrorApp.UPDATE_ORDER_STATUS_FAIL);
    }


    public void deleteOrder(Integer id) {
        Order order = getOrderById(id);

        orderRepository.deleteById(id);
    }


}
