package com.LaptopWeb.mapper;

import com.LaptopWeb.dto.request.ProductRequest;
import com.LaptopWeb.dto.response.ProductResponse;
import com.LaptopWeb.entity.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Map;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "productDetails", expression = "java(buildJsonToString(request))")
    Product toProduct(ProductRequest request);

    @Mapping(target = "productDetails", expression = "java(buildStringToJson(product))")
    @Mapping(target = "category", expression = "java(buildCategoryName(product))")
    @Mapping(target = "brand", expression = "java(buildBrandName(product))")
    @Mapping(target = "image", expression = "java(buildImageName(product))")
    ProductResponse toProductResponse(Product product);


    default String buildJsonToString(ProductRequest request) {
        try {
            Map<String, Object> productDetail = request.getProductDetails();

            return new ObjectMapper().writeValueAsString(productDetail);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    default JsonNode buildStringToJson(Product product) {
        try {
            String productDetail = product.getProductDetails();

            return new ObjectMapper().readTree(productDetail);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    default String buildCategoryName(Product product) {
        return product.getCategory() != null
                ? product.getCategory().getName()
                : null;
    }

    default String buildBrandName(Product product) {
        return product.getBrand() != null
                ? product.getBrand().getName()
                : null;
    }

    default String buildImageName(Product product) {
        return "https://ecommerce-vinhseo.s3.ap-southeast-2.amazonaws.com/products/"
                + product.getId() + "/"
                + product.getImage();
    }


}
