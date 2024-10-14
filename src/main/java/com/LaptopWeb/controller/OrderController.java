package com.LaptopWeb.controller;

import com.LaptopWeb.dto.request.ChangeOrderStatusRequest;
import com.LaptopWeb.dto.request.OrderRequest;
import com.LaptopWeb.dto.response.ApiResponse;
import com.LaptopWeb.dto.response.OrderDetailResponse;
import com.LaptopWeb.dto.response.OrderResponse;
import com.LaptopWeb.entity.Order;
import com.LaptopWeb.entity.OrderDetail;
import com.LaptopWeb.mapper.OrderDetailMapper;
import com.LaptopWeb.mapper.OrderMapper;
import com.LaptopWeb.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    private OrderResponse orderToOrderResponse(Order order) {
        List<OrderDetail> orderDetails = order.getOrderDetails();

        List<OrderDetailResponse> orderDetailResponses = orderDetails.stream()
                .map(orderDetailMapper::toOrderDetailResponse).toList();

        OrderResponse orderResponse = orderMapper.toOrderResponse(order);
        orderResponse.setOrderDetailResponses(orderDetailResponses);

        return orderResponse;
    }


    @PostMapping
    public ResponseEntity<?> createOrder(@Valid @RequestPart("order")OrderRequest request) {
        Order order = orderService.createOrder(request, "vinhseo");

        OrderResponse orderResponse = orderToOrderResponse(order);

        // send message orderResponse after create an order
        simpMessagingTemplate.convertAndSend("/notice/admin", orderResponse);

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .success(true)
                .content(orderResponse)
                .build();

        return ResponseEntity.ok().body(apiResponse);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable("id") Integer id) {
        Order order = orderService.getOrderById(id);

        OrderResponse orderResponse = orderToOrderResponse(order);

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .success(true)
                .content(orderResponse)
                .build();

        return ResponseEntity.ok().body(apiResponse);
    }

    @GetMapping("/code/{orderCode}")
    public ResponseEntity<?> getOrderByOrderCode(@PathVariable("orderCode") String orderCode) {
        Order order = orderService.getOrderByOrderCode(orderCode);

        OrderResponse orderResponse = orderToOrderResponse(order);

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .success(true)
                .content(orderResponse)
                .build();

        return ResponseEntity.ok().body(apiResponse);
    }


    @PutMapping("/code/{orderCode}/change-status")
    public ResponseEntity<?> updateOrder(
            @PathVariable String orderCode,
            @RequestPart("changeOrderStatus") ChangeOrderStatusRequest request
    ) {
        Order order = orderService.updateStatus(request.getNewOrderStatus(), orderCode);

        OrderResponse orderResponse = orderToOrderResponse(order);

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .success(true)
                .content(orderResponse)
                .build();

        return ResponseEntity.ok().body(apiResponse);
    }

    @GetMapping("/my-orders")
    public ResponseEntity<?> getMyOrders(@AuthenticationPrincipal Jwt jwt) {
        List<Order> orders = orderService.getMyOrder(jwt.getSubject());

        List<OrderResponse> orderResponses = orders.stream().map(this::orderToOrderResponse).toList();

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .success(true)
                .content(orderResponses)
                .build();

        return ResponseEntity.ok().body(apiResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable("id") Integer id) {
        orderService.deleteOrder(id);

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .success(true)
                .content("Delete order successful")
                .build();

        return ResponseEntity.ok().body(apiResponse);
    }
}
