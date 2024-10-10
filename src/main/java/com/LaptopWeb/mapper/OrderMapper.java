package com.LaptopWeb.mapper;

import com.LaptopWeb.dto.request.OrderRequest;
import com.LaptopWeb.dto.response.OrderResponse;
import com.LaptopWeb.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "statusOrder", ignore = true)
    Order toOrder(OrderRequest request);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "statusOrderId", source = "statusOrder.id")
    OrderResponse toOrderResponse(Order order);


}
