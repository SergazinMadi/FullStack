package org.example.fullstack.db.repository;

import org.example.fullstack.db.model.Product;
import org.example.fullstack.db.model.ProductStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductStatusHistoryRepository extends JpaRepository<ProductStatusHistory, Long> {
    @Override
    Optional<ProductStatusHistory> findById(Long id);

    Optional<ProductStatusHistory> create();
}