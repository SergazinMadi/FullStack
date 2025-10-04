package org.example.fullstack.db.dto.request;

import org.example.fullstack.db.enums.OrderStatus;
import org.example.fullstack.db.enums.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Request DTO for updating Order
 */
public record OrderUpdateRequest(
    Long driverId,
    Long fromPointId,
    Long toPointId,
    OrderStatus status,
    LocalDateTime pickupDatePlanned,
    LocalDateTime pickupDateActual,
    LocalDateTime deliveryDatePlanned,
    LocalDateTime deliveryDateActual,
    LocalDateTime arrivedAtPickupPoint,
    BigDecimal deliveryCost,
    Boolean isPaid,
    PaymentMethod paymentMethod,
    String notes,
    String specialInstructions,
    String currentLocation
) {}
