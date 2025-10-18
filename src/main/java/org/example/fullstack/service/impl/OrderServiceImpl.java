package org.example.fullstack.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.fullstack.db.dto.dto.OrderDto;
import org.example.fullstack.db.dto.request.OrderCreateRequest;
import org.example.fullstack.db.dto.request.OrderUpdateRequest;
import org.example.fullstack.db.enums.OrderStatus;
import org.example.fullstack.db.mapper.OrderMapper;
import org.example.fullstack.db.model.Order;
import org.example.fullstack.db.model.User;
import org.example.fullstack.db.repository.OrderRepository;
import org.example.fullstack.db.repository.UserRepository;
import org.example.fullstack.service.OrderService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public OrderDto createOrder(User user, OrderCreateRequest request) {
        log.info("Creating order for user: {}", user.getUsername());
        
        Order order = orderMapper.orderCreateRequestToOrder(request);
        order.setOrderNumber(generateOrderNumber());
        order.setStatus(OrderStatus.CREATED);
        
        Order savedOrder = orderRepository.save(order);
        log.info("Order created with ID: {}", savedOrder.getId());
        
        return orderMapper.orderToOrderDto(savedOrder);
    }

    @Override
    @Transactional
    public OrderDto updateOrder(Long orderId, OrderUpdateRequest request) {
        log.info("Updating order with ID: {}", orderId);
        
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));
        
        orderMapper.updateOrderFromRequest(request, order);
        Order updatedOrder = orderRepository.save(order);
        
        log.info("Order updated successfully");
        return orderMapper.orderToOrderDto(updatedOrder);
    }

    @Override
    public OrderDto getOrder(Long orderId) {
        log.info("Getting order with ID: {}", orderId);
        
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));
        
        return orderMapper.orderToOrderDto(order);
    }

    @Override
    public List<OrderDto> getOrdersByDriver(User driver) {
        log.info("Getting orders for driver: {}", driver.getUsername());
        
        List<Order> orders = orderRepository.findByDriver(driver);
        return orders.stream()
                .map(orderMapper::orderToOrderDto)
                .toList();
    }

    @Override
    public List<OrderDto> getOrdersByStatus(OrderStatus status) {
        log.info("Getting orders with status: {}", status);
        
        List<Order> orders = orderRepository.findByStatus(status);
        return orders.stream()
                .map(orderMapper::orderToOrderDto)
                .toList();
    }

    @Override
    public List<OrderDto> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Getting orders by date range: {} to {}", startDate, endDate);
        
        List<Order> orders = orderRepository.findOrdersByPickupDateRange(startDate, endDate);
        return orders.stream()
                .map(orderMapper::orderToOrderDto)
                .toList();
    }

    @Override
    @Transactional
    public OrderDto updateOrderStatus(Long orderId, OrderStatus status) {
        log.info("Updating order status for order ID: {} to {}", orderId, status);
        
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));
        
        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);
        
        log.info("Order status updated successfully");
        return orderMapper.orderToOrderDto(updatedOrder);
    }

    @Override
    @Transactional
    public OrderDto assignDriver(Long orderId, Long driverId) {
        log.info("Assigning driver {} to order {}", driverId, orderId);
        
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));
        
        User driver = userRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found with ID: " + driverId));
        
        order.setDriver(driver);
        order.setStatus(OrderStatus.ASSIGNED_TO_DRIVER);
        
        Order updatedOrder = orderRepository.save(order);
        
        log.info("Driver assigned successfully");
        return orderMapper.orderToOrderDto(updatedOrder);
    }

    @Override
    @Transactional
    public void deleteOrder(Long orderId) {
        log.info("Deleting order with ID: {}", orderId);
        
        if (!orderRepository.existsById(orderId)) {
            throw new RuntimeException("Order not found with ID: " + orderId);
        }
        
        orderRepository.deleteById(orderId);
        log.info("Order deleted successfully");
    }

    @Override
    public List<OrderDto> getOverdueOrders() {
        log.info("Getting overdue orders");
        
        List<Order> orders = orderRepository.findOverdueUnpaidOrders(LocalDateTime.now());
        return orders.stream()
                .map(orderMapper::orderToOrderDto)
                .toList();
    }

    @Override
    public OrderDto getOrderByNumber(String orderNumber) {
        log.info("Getting order by number: {}", orderNumber);
        
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new RuntimeException("Order not found with number: " + orderNumber));
        
        return orderMapper.orderToOrderDto(order);
    }

    private String generateOrderNumber() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
