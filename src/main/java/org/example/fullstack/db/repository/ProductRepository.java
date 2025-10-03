package org.example.fullstack.db.repository;

import org.example.fullstack.db.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
