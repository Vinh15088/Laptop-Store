package com.LaptopWeb.controller;

import com.LaptopWeb.dto.request.OrderRequest;
import com.LaptopWeb.dto.response.ApiResponse;
import com.LaptopWeb.dto.response.OrderResponse;
import com.LaptopWeb.entity.Order;
import com.LaptopWeb.mapper.OrderMapper;
import com.LaptopWeb.service.OrderService;
import com.LaptopWeb.utils.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderMapper orderMapper;

    private static final String PAGE_SIZE = "10";
    private static final String PAGE_NUMBER = "1";

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestPart("order")OrderRequest request) {
        Order order = orderService.createOrder(request);

        OrderResponse orderResponse = orderMapper.toOrderResponse(order);

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .success(true)
                .content(orderResponse)
                .build();

        return ResponseEntity.ok().body(apiResponse);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable("id") Integer id) {
        Order order = orderService.getOrderById(id);

        OrderResponse orderResponse = orderMapper.toOrderResponse(order);

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .success(true)
                .content(orderResponse)
                .build();

        return ResponseEntity.ok().body(apiResponse);
    }

    @GetMapping("/code/{orderCode}")
    public ResponseEntity<?> getOrderByOrderCode(@PathVariable("orderCode") String orderCode) {
        Order order = orderService.getOrderByOrderCode(orderCode);

        OrderResponse orderResponse = orderMapper.toOrderResponse(order);

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .success(true)
                .content(orderResponse)
                .build();

        return ResponseEntity.ok().body(apiResponse);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllOrder(
            @RequestParam(name = "size", defaultValue = PAGE_SIZE) Integer size,
            @RequestParam(name = "number", defaultValue = PAGE_NUMBER) Integer number,
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(name = "order", defaultValue = "asc") String order
    ) {
        Page<Order> orderPage = orderService.getAllOrder(number-1, size, sortBy, order);

        List<Order> orders = orderPage.getContent();
        List<OrderResponse> orderResponses = orders.stream().map(orderMapper::toOrderResponse).toList();

        PageInfo pageInfo = PageInfo.builder()
                .page(orderPage.getNumber()+1)
                .size(orderPage.getSize())
                .totalElements(orderPage.getNumberOfElements())
                .totalPages(orderPage.getTotalPages())
                .build();

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .success(true)
                .content(orderResponses)
                .pageInfo(pageInfo)
                .build();

        return ResponseEntity.ok().body(apiResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(
            @PathVariable("id") Integer id,
            @RequestPart("order") OrderRequest request
    ) {
        Order order = orderService.updateOrder(id, request);

        OrderResponse orderResponse = orderMapper.toOrderResponse(order);

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .success(true)
                .content(orderResponse)
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
