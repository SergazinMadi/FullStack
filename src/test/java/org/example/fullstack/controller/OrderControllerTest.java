package org.example.fullstack.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.fullstack.db.dto.dto.OrderDto;
import org.example.fullstack.db.dto.request.OrderCreateRequest;
import org.example.fullstack.db.dto.request.OrderUpdateRequest;
import org.example.fullstack.db.enums.OrderStatus;
import org.example.fullstack.db.enums.PaymentMethod;
import org.example.fullstack.db.enums.UserRole;
import org.example.fullstack.db.model.User;
import org.example.fullstack.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;
    private OrderCreateRequest orderCreateRequest;
    private OrderUpdateRequest orderUpdateRequest;
    private OrderDto orderDto;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setRole(UserRole.CLIENT);

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
    @WithMockUser(authorities = "CLIENT")
    void createOrder_ShouldReturnOrderDto_WhenValidRequest() throws Exception {
        // Given
        when(orderService.createOrder(any(User.class), any(OrderCreateRequest.class))).thenReturn(orderDto);

        // When & Then
        mockMvc.perform(post("/orders")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderCreateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.orderNumber").value("ORD-12345678"))
                .andExpect(jsonPath("$.status").value("CREATED"));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateOrder_ShouldReturnOrderDto_WhenValidRequest() throws Exception {
        // Given
        when(orderService.updateOrder(anyLong(), any(OrderUpdateRequest.class))).thenReturn(orderDto);

        // When & Then
        mockMvc.perform(put("/orders/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderUpdateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.orderNumber").value("ORD-12345678"));
    }

    @Test
    void getOrder_ShouldReturnOrderDto_WhenOrderExists() throws Exception {
        // Given
        when(orderService.getOrder(1L)).thenReturn(orderDto);

        // When & Then
        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.orderNumber").value("ORD-12345678"));
    }

    @Test
    void getOrderByNumber_ShouldReturnOrderDto_WhenOrderExists() throws Exception {
        // Given
        when(orderService.getOrderByNumber("ORD-12345678")).thenReturn(orderDto);

        // When & Then
        mockMvc.perform(get("/orders/number/ORD-12345678"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.orderNumber").value("ORD-12345678"));
    }

    @Test
    @WithMockUser(authorities = "DRIVER")
    void getOrdersByDriver_ShouldReturnListOfOrderDtos() throws Exception {
        // Given
        List<OrderDto> orders = List.of(orderDto);
        when(orderService.getOrdersByDriver(any(User.class))).thenReturn(orders);

        // When & Then
        mockMvc.perform(get("/orders/driver"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].orderNumber").value("ORD-12345678"));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void getOrdersByStatus_ShouldReturnListOfOrderDtos() throws Exception {
        // Given
        List<OrderDto> orders = List.of(orderDto);
        when(orderService.getOrdersByStatus(OrderStatus.CREATED)).thenReturn(orders);

        // When & Then
        mockMvc.perform(get("/orders/status/CREATED"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].status").value("CREATED"));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void getOrdersByDateRange_ShouldReturnListOfOrderDtos() throws Exception {
        // Given
        List<OrderDto> orders = List.of(orderDto);
        when(orderService.getOrdersByDateRange(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(orders);

        // When & Then
        mockMvc.perform(get("/orders/date-range")
                        .param("startDate", "2024-01-01T00:00:00")
                        .param("endDate", "2024-12-31T23:59:59"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateOrderStatus_ShouldReturnOrderDto_WhenValidRequest() throws Exception {
        // Given
        when(orderService.updateOrderStatus(1L, OrderStatus.ASSIGNED_TO_DRIVER)).thenReturn(orderDto);

        // When & Then
        mockMvc.perform(put("/orders/1/status")
                        .with(csrf())
                        .param("status", "ASSIGNED_TO_DRIVER"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void assignDriver_ShouldReturnOrderDto_WhenValidRequest() throws Exception {
        // Given
        when(orderService.assignDriver(1L, 2L)).thenReturn(orderDto);

        // When & Then
        mockMvc.perform(put("/orders/1/assign-driver")
                        .with(csrf())
                        .param("driverId", "2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void getOverdueOrders_ShouldReturnListOfOrderDtos() throws Exception {
        // Given
        List<OrderDto> orders = List.of(orderDto);
        when(orderService.getOverdueOrders()).thenReturn(orders);

        // When & Then
        mockMvc.perform(get("/orders/overdue"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void deleteOrder_ShouldReturnNoContent_WhenOrderExists() throws Exception {
        // Given
        // When & Then
        mockMvc.perform(delete("/orders/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    void createOrder_ShouldReturnForbidden_WhenUserNotAuthorized() throws Exception {
        // Given
        // When & Then
        mockMvc.perform(post("/orders")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderCreateRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "CLIENT")
    void updateOrder_ShouldReturnForbidden_WhenUserNotAuthorized() throws Exception {
        // Given
        // When & Then
        mockMvc.perform(put("/orders/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderUpdateRequest)))
                .andExpect(status().isForbidden());
    }
}
