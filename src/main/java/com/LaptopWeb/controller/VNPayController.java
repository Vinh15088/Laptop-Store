package com.LaptopWeb.controller;

import com.LaptopWeb.dto.response.ApiResponse;
import com.LaptopWeb.dto.response.VNPayResponse;
import com.LaptopWeb.service.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment/vn-pay")
public class VNPayController {
    @Autowired
    private VNPayService vnPayService;

    @GetMapping("/payment-order/{orderCode}")
    public ResponseEntity<?> paymentOrder(@PathVariable("orderCode") String orderCode) {
        VNPayResponse vnPayResponse = vnPayService.paymentOrder(orderCode);

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .success(true)
                .content(vnPayResponse)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/call-back")
    public ResponseEntity<?> callBack(HttpServletRequest request) {
        VNPayResponse vnPayResponse = vnPayService.orderReturn(request);

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .success(true)
                .content(vnPayResponse)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/IPN")
    public ResponseEntity<?> IPN(HttpServletRequest request) {
        VNPayResponse vnPayResponse = vnPayService.orderReturn(request);

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .success(true)
                .content(vnPayResponse)
                .build();

        return ResponseEntity.ok(apiResponse);
    }


}
