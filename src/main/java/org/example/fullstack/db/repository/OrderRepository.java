package org.example.fullstack.db.repository;

import org.example.fullstack.db.enums.OrderStatus;
import org.example.fullstack.db.model.Order;
import org.example.fullstack.db.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOrderNumber(String orderNumber);
    
    List<Order> findByDriver(User driver);
    
    List<Order> findByStatus(OrderStatus status);
    
    List<Order> findByDriverAndStatus(User driver, OrderStatus status);
    
    @Query("SELECT o FROM Order o WHERE o.pickupDatePlanned BETWEEN :startDate AND :endDate")
    List<Order> findOrdersByPickupDateRange(@Param("startDate") LocalDateTime startDate, 
                                          @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT o FROM Order o WHERE o.deliveryDatePlanned BETWEEN :startDate AND :endDate")
    List<Order> findOrdersByDeliveryDateRange(@Param("startDate") LocalDateTime startDate, 
                                            @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT o FROM Order o WHERE o.isPaid = false AND o.deliveryDatePlanned < :currentDate")
    List<Order> findOverdueUnpaidOrders(@Param("currentDate") LocalDateTime currentDate);
    
    @Query("SELECT COUNT(o) FROM Order o WHERE o.driver = :driver AND o.status = :status")
    Long countByDriverAndStatus(@Param("driver") User driver, @Param("status") OrderStatus status);
}