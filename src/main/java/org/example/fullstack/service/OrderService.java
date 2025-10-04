package org.example.fullstack.service;

import org.example.fullstack.db.dto.dto.OrderDto;
import org.example.fullstack.db.dto.request.OrderCreateRequest;
import org.example.fullstack.db.dto.request.OrderUpdateRequest;
import org.example.fullstack.db.enums.OrderStatus;
import org.example.fullstack.db.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
    OrderDto createOrder(User user, OrderCreateRequest request);
    OrderDto updateOrder(Long orderId, OrderUpdateRequest request);
    OrderDto getOrder(Long orderId);
    List<OrderDto> getOrdersByDriver(User driver);
    List<OrderDto> getOrdersByStatus(OrderStatus status);
    List<OrderDto> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    OrderDto updateOrderStatus(Long orderId, OrderStatus status);
    OrderDto assignDriver(Long orderId, Long driverId);
    void deleteOrder(Long orderId);
    List<OrderDto> getOverdueOrders();
    OrderDto getOrderByNumber(String orderNumber);
}
