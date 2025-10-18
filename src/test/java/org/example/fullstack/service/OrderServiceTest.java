package org.example.fullstack.service;

import org.example.fullstack.db.dto.dto.OrderDto;
import org.example.fullstack.db.dto.request.OrderCreateRequest;
import org.example.fullstack.db.dto.request.OrderUpdateRequest;
import org.example.fullstack.db.enums.OrderStatus;
import org.example.fullstack.db.enums.PaymentMethod;
import org.example.fullstack.db.mapper.OrderMapper;
import org.example.fullstack.db.model.Order;
import org.example.fullstack.db.model.User;
import org.example.fullstack.db.repository.OrderRepository;
import org.example.fullstack.db.repository.UserRepository;
import org.example.fullstack.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private User testUser;
    private Order testOrder;
    private OrderCreateRequest orderCreateRequest;
    private OrderUpdateRequest orderUpdateRequest;
    private OrderDto orderDto;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setRole(org.example.fullstack.db.enums.UserRole.CLIENT);

        testOrder = new Order();
        testOrder.setId(1L);
        testOrder.setOrderNumber("ORD-12345678");
        testOrder.setStatus(OrderStatus.CREATED);
        testOrder.setPickupDatePlanned(LocalDateTime.now().plusDays(1));
        testOrder.setDeliveryDatePlanned(LocalDateTime.now().plusDays(2));
        testOrder.setDeliveryCost(new BigDecimal("100.00"));
        testOrder.setPaymentMethod(PaymentMethod.CARD);

        orderCreateRequest = new OrderCreateRequest(
                1L, // driverId
                1L, // fromPointId
                2L, // toPointId
                LocalDateTime.now().plusDays(1), // pickupDatePlanned
                LocalDateTime.now().plusDays(2), // deliveryDatePlanned
                new BigDecimal("100.00"), // deliveryCost
                PaymentMethod.CARD, // paymentMethod
                "Test notes", // notes
                "Special instructions" // specialInstructions
        );

        orderUpdateRequest = new OrderUpdateRequest(
                1L, // driverId
                1L, // fromPointId
                2L, // toPointId
                OrderStatus.ASSIGNED_TO_DRIVER, // status
                LocalDateTime.now().plusDays(1), // pickupDatePlanned
                null, // pickupDateActual
                LocalDateTime.now().plusDays(2), // deliveryDatePlanned
                null, // deliveryDateActual
                null, // arrivedAtPickupPoint
                new BigDecimal("120.00"), // deliveryCost
                false, // isPaid
                PaymentMethod.CARD, // paymentMethod
                "Updated notes", // notes
                "Updated instructions", // specialInstructions
                "Current location" // currentLocation
        );

        orderDto = new OrderDto(
                1L,
                "ORD-12345678",
                1L,
                1L,
                2L,
                OrderStatus.CREATED,
                LocalDateTime.now().plusDays(1),
                null,
                LocalDateTime.now().plusDays(2),
                null,
                null,
                new BigDecimal("100.00"),
                false,
                PaymentMethod.CARD,
                "Test notes",
                "Special instructions",
                null,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Test
    void createOrder_ShouldReturnOrderDto_WhenValidRequest() {
        // Given
        when(orderMapper.orderCreateRequestToOrder(any(OrderCreateRequest.class))).thenReturn(testOrder);
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);
        when(orderMapper.orderToOrderDto(any(Order.class))).thenReturn(orderDto);

        // When
        OrderDto result = orderService.createOrder(testUser, orderCreateRequest);

        // Then
        assertNotNull(result);
        assertEquals(orderDto, result);
        verify(orderMapper).orderCreateRequestToOrder(orderCreateRequest);
        verify(orderRepository).save(testOrder);
        verify(orderMapper).orderToOrderDto(testOrder);
    }

    @Test
    void updateOrder_ShouldReturnUpdatedOrderDto_WhenOrderExists() {
        // Given
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);
        when(orderMapper.orderToOrderDto(any(Order.class))).thenReturn(orderDto);

        // When
        OrderDto result = orderService.updateOrder(1L, orderUpdateRequest);

        // Then
        assertNotNull(result);
        assertEquals(orderDto, result);
        verify(orderRepository).findById(1L);
        verify(orderMapper).updateOrderFromRequest(orderUpdateRequest, testOrder);
        verify(orderRepository).save(testOrder);
        verify(orderMapper).orderToOrderDto(testOrder);
    }

    @Test
    void updateOrder_ShouldThrowException_WhenOrderNotFound() {
        // Given
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            orderService.updateOrder(1L, orderUpdateRequest);
        });

        verify(orderRepository).findById(1L);
        verify(orderMapper, never()).updateOrderFromRequest(any(), any());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void getOrder_ShouldReturnOrderDto_WhenOrderExists() {
        // Given
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(orderMapper.orderToOrderDto(any(Order.class))).thenReturn(orderDto);

        // When
        OrderDto result = orderService.getOrder(1L);

        // Then
        assertNotNull(result);
        assertEquals(orderDto, result);
        verify(orderRepository).findById(1L);
        verify(orderMapper).orderToOrderDto(testOrder);
    }

    @Test
    void getOrder_ShouldThrowException_WhenOrderNotFound() {
        // Given
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            orderService.getOrder(1L);
        });

        verify(orderRepository).findById(1L);
        verify(orderMapper, never()).orderToOrderDto(any());
    }

    @Test
    void getOrdersByDriver_ShouldReturnListOfOrderDtos() {
        // Given
        List<Order> orders = List.of(testOrder);
        when(orderRepository.findByDriver(testUser)).thenReturn(orders);
        when(orderMapper.orderToOrderDto(any(Order.class))).thenReturn(orderDto);

        // When
        List<OrderDto> result = orderService.getOrdersByDriver(testUser);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(orderDto, result.get(0));
        verify(orderRepository).findByDriver(testUser);
        verify(orderMapper).orderToOrderDto(testOrder);
    }

    @Test
    void getOrdersByStatus_ShouldReturnListOfOrderDtos() {
        // Given
        List<Order> orders = List.of(testOrder);
        when(orderRepository.findByStatus(OrderStatus.CREATED)).thenReturn(orders);
        when(orderMapper.orderToOrderDto(any(Order.class))).thenReturn(orderDto);

        // When
        List<OrderDto> result = orderService.getOrdersByStatus(OrderStatus.CREATED);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(orderDto, result.get(0));
        verify(orderRepository).findByStatus(OrderStatus.CREATED);
        verify(orderMapper).orderToOrderDto(testOrder);
    }

    @Test
    void updateOrderStatus_ShouldReturnUpdatedOrderDto_WhenOrderExists() {
        // Given
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);
        when(orderMapper.orderToOrderDto(any(Order.class))).thenReturn(orderDto);

        // When
        OrderDto result = orderService.updateOrderStatus(1L, OrderStatus.ASSIGNED_TO_DRIVER);

        // Then
        assertNotNull(result);
        assertEquals(orderDto, result);
        verify(orderRepository).findById(1L);
        verify(orderRepository).save(testOrder);
        verify(orderMapper).orderToOrderDto(testOrder);
    }

    @Test
    void assignDriver_ShouldReturnUpdatedOrderDto_WhenOrderAndDriverExist() {
        // Given
        User driver = new User();
        driver.setId(2L);
        driver.setRole(org.example.fullstack.db.enums.UserRole.DRIVER);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(userRepository.findById(2L)).thenReturn(Optional.of(driver));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);
        when(orderMapper.orderToOrderDto(any(Order.class))).thenReturn(orderDto);

        // When
        OrderDto result = orderService.assignDriver(1L, 2L);

        // Then
        assertNotNull(result);
        assertEquals(orderDto, result);
        verify(orderRepository).findById(1L);
        verify(userRepository).findById(2L);
        verify(orderRepository).save(testOrder);
        verify(orderMapper).orderToOrderDto(testOrder);
    }

    @Test
    void deleteOrder_ShouldDeleteOrder_WhenOrderExists() {
        // Given
        when(orderRepository.existsById(1L)).thenReturn(true);

        // When
        orderService.deleteOrder(1L);

        // Then
        verify(orderRepository).existsById(1L);
        verify(orderRepository).deleteById(1L);
    }

    @Test
    void deleteOrder_ShouldThrowException_WhenOrderNotFound() {
        // Given
        when(orderRepository.existsById(1L)).thenReturn(false);

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            orderService.deleteOrder(1L);
        });

        verify(orderRepository).existsById(1L);
        verify(orderRepository, never()).deleteById(anyLong());
    }

    @Test
    void getOverdueOrders_ShouldReturnListOfOverdueOrders() {
        // Given
        List<Order> orders = List.of(testOrder);
        when(orderRepository.findOverdueUnpaidOrders(any(LocalDateTime.class))).thenReturn(orders);
        when(orderMapper.orderToOrderDto(any(Order.class))).thenReturn(orderDto);

        // When
        List<OrderDto> result = orderService.getOverdueOrders();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(orderDto, result.get(0));
        verify(orderRepository).findOverdueUnpaidOrders(any(LocalDateTime.class));
        verify(orderMapper).orderToOrderDto(testOrder);
    }

    @Test
    void getOrderByNumber_ShouldReturnOrderDto_WhenOrderExists() {
        // Given
        when(orderRepository.findByOrderNumber("ORD-12345678")).thenReturn(Optional.of(testOrder));
        when(orderMapper.orderToOrderDto(any(Order.class))).thenReturn(orderDto);

        // When
        OrderDto result = orderService.getOrderByNumber("ORD-12345678");

        // Then
        assertNotNull(result);
        assertEquals(orderDto, result);
        verify(orderRepository).findByOrderNumber("ORD-12345678");
        verify(orderMapper).orderToOrderDto(testOrder);
    }

    @Test
    void getOrderByNumber_ShouldThrowException_WhenOrderNotFound() {
        // Given
        when(orderRepository.findByOrderNumber("ORD-12345678")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            orderService.getOrderByNumber("ORD-12345678");
        });

        verify(orderRepository).findByOrderNumber("ORD-12345678");
        verify(orderMapper, never()).orderToOrderDto(any());
    }
}
