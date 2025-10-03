package org.example.fullstack.db.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.fullstack.db.enums.ProductStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_status_history")
@Setter
@Getter
public class ProductStatusHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status;

    @Column(nullable = false)
    private LocalDateTime changedAt;
}
