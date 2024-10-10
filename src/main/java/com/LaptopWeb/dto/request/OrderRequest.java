package com.LaptopWeb.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderRequest {

    @NotBlank(message = "ORDER_ADDRESS_NOT_BLANK")
    String address;

    Long totalPrice;

    String note;

    boolean paymentStatus;

    @NotBlank(message = "ORDER_PAYMENT_TYPE_NOT_BLANK")
    String paymentType;


    Integer userId;
    Integer statusOrderId;
}
