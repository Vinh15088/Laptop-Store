package com.LaptopWeb.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
    Integer id;

    String orderCode;

    String address;

    Date createdAt;

    Long totalPrice;

    String note;

    String paymentStatus;

    String paymentType;

    Integer statusOrderId;

    Integer userId;
}
