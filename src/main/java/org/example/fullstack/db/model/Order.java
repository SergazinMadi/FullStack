package org.example.fullstack.db.model;

import jakarta.persistence.*;
import org.example.fullstack.db.enums.OrderStatus;
import org.example.fullstack.db.enums.PaymentMethod;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "order_number", unique = true, length = 20)
    private String orderNumber;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
    private User driver; // Водитель (назначается позже)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_point_id")
    private PickupPoint fromPoint; // Может быть null для адресной доставки

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_point_id")
    private PickupPoint toPoint;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.CREATED;

    @Column(name = "pickup_date_planned")
    private LocalDateTime pickupDatePlanned; // Планируемая дата забора
    
    @Column(name = "pickup_date_actual")
    private LocalDateTime pickupDateActual; // Фактическая дата забора
    
    @Column(name = "delivery_date_planned")
    private LocalDateTime deliveryDatePlanned; // Планируемая дата доставки
    
    @Column(name = "delivery_date_actual")
    private LocalDateTime deliveryDateActual; // Фактическая дата доставки
    
    @Column(name = "arrived_at_pickup_point")
    private LocalDateTime arrivedAtPickupPoint; // Когда товар прибыл в пункт выдачи
    
    @Column(name = "delivery_cost", precision = 10, scale = 2)
    private BigDecimal deliveryCost;
    
    @Column(name = "is_paid")
    private Boolean isPaid = false;
    
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    @Column(name = "special_instructions", columnDefinition = "TEXT")
    private String specialInstructions; // Особые указания по доставке

    @Column(name = "current_location", columnDefinition = "TEXT")
    private String currentLocation;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    // Методы для расчета метрик
    public Duration getDeliveryTime() {
        if (pickupDateActual != null && deliveryDateActual != null) {
            return Duration.between(pickupDateActual, deliveryDateActual);
        }
        return null;
    }
    
    public boolean isOverdue() {
        if (deliveryDatePlanned == null) return false;
        if (status == OrderStatus.DELIVERED) return false;
        return LocalDateTime.now().isAfter(deliveryDatePlanned);
    }
}



