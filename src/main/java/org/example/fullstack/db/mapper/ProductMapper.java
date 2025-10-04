package org.example.fullstack.db.mapper;


import org.example.fullstack.db.dto.dto.ProductDto;
import org.example.fullstack.db.dto.request.ProductCreateRequest;
import org.example.fullstack.db.dto.request.RegistrationRequest;
import org.example.fullstack.db.model.Product;
import org.example.fullstack.db.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = ProductMapper.class)
public interface ProductMapper {
    Product productCreateRequestToProduct(ProductCreateRequest request);
    ProductDto productToProductDto(Product product);
}