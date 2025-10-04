package org.example.fullstack.controller;

import lombok.RequiredArgsConstructor;
import org.example.fullstack.db.dto.dto.OrderDto;
import org.example.fullstack.db.dto.request.OrderCreateRequest;
import org.example.fullstack.db.dto.request.OrderUpdateRequest;
import org.example.fullstack.db.enums.OrderStatus;
import org.example.fullstack.db.model.User;
import org.example.fullstack.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('CLIENT', 'ADMIN')")
    public ResponseEntity<OrderDto> createOrder(@AuthenticationPrincipal User user, 
                                               @RequestBody OrderCreateRequest request) {
        OrderDto order = orderService.createOrder(user, request);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/{orderId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<OrderDto> updateOrder(@PathVariable Long orderId, 
                                               @RequestBody OrderUpdateRequest request) {
        OrderDto order = orderService.updateOrder(orderId, request);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable Long orderId) {
        OrderDto order = orderService.getOrder(orderId);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/number/{orderNumber}")
    public ResponseEntity<OrderDto> getOrderByNumber(@PathVariable String orderNumber) {
        OrderDto order = orderService.getOrderByNumber(orderNumber);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/driver")
    @PreAuthorize("hasAnyAuthority('DRIVER', 'ADMIN')")
    public ResponseEntity<List<OrderDto>> getOrdersByDriver(@AuthenticationPrincipal User driver) {
        List<OrderDto> orders = orderService.getOrdersByDriver(driver);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<List<OrderDto>> getOrdersByStatus(@PathVariable OrderStatus status) {
        List<OrderDto> orders = orderService.getOrdersByStatus(status);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/date-range")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<List<OrderDto>> getOrdersByDateRange(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {
        List<OrderDto> orders = orderService.getOrdersByDateRange(startDate, endDate);
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/{orderId}/status")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'DRIVER')")
    public ResponseEntity<OrderDto> updateOrderStatus(@PathVariable Long orderId, 
                                                     @RequestParam OrderStatus status) {
        OrderDto order = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/{orderId}/assign-driver")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<OrderDto> assignDriver(@PathVariable Long orderId, 
                                                @RequestParam Long driverId) {
        OrderDto order = orderService.assignDriver(orderId, driverId);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/overdue")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<List<OrderDto>> getOverdueOrders() {
        List<OrderDto> orders = orderService.getOverdueOrders();
        return ResponseEntity.ok(orders);
    }

    @DeleteMapping("/{orderId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }
}
