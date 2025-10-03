package org.example.fullstack.db.repository;

import org.example.fullstack.db.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
