package org.example.fullstack.db.dto.request;

import org.example.fullstack.db.enums.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Request DTO for creating Order
 */
public record OrderCreateRequest(
    Long driverId,
    Long fromPointId,
    Long toPointId,
    LocalDateTime pickupDatePlanned,
    LocalDateTime deliveryDatePlanned,
    BigDecimal deliveryCost,
    PaymentMethod paymentMethod,
    String notes,
    String specialInstructions
) {}
