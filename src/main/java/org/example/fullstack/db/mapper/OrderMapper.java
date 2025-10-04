package org.example.fullstack.db.mapper;

import org.example.fullstack.db.dto.dto.OrderDto;
import org.example.fullstack.db.dto.request.OrderCreateRequest;
import org.example.fullstack.db.dto.request.OrderUpdateRequest;
import org.example.fullstack.db.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {
    Order orderCreateRequestToOrder(OrderCreateRequest request);
    OrderDto orderToOrderDto(Order order);
    void updateOrderFromRequest(OrderUpdateRequest request, @org.mapstruct.MappingTarget Order order);
}
