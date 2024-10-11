package com.LaptopWeb.mapper;

import com.LaptopWeb.dto.request.OrderRequest;
import com.LaptopWeb.dto.response.OrderResponse;
import com.LaptopWeb.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    Order toOrder(OrderRequest request);

    @Mapping(target = "fullName", source = "user.fullName")
    @Mapping(target = "orderStatus", source = "orderStatus.name")
//    @Mapping(target = "orderDetailResponses", ignore = true)
    OrderResponse toOrderResponse(Order order);

}
