package com.LaptopWeb.dto.request;

import com.LaptopWeb.entity.Brand;
import com.LaptopWeb.entity.Category;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRequest {

    @NotBlank(message = "PRODUCT_NAME_NOT_BLANK")
    String name;

    @NotBlank(message = "PRODUCT_SKU_NOT_BLANK")
    String sku;

    String shortDescription;
    String longDescription;

    @Size(min = 0, message = "PRODUCT_STOCK_MIN_0")
    @NotNull(message = "PRODUCT_STOCK_NOT_NULL")
    Integer stock;

    Long cost;
    Long price;
    Integer discount;

    boolean active;

    Map<String, Object> productDetails;

    Integer category_id;
    Integer brand_id;
}
