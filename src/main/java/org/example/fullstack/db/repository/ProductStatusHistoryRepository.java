package org.example.fullstack.db.repository;

import org.example.fullstack.db.enums.ProductStatus;
import org.example.fullstack.db.model.Product;
import org.example.fullstack.db.model.ProductStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductStatusHistoryRepository extends JpaRepository<ProductStatusHistory, Long> {
    List<ProductStatusHistory> findByProduct(Product product);
    
    List<ProductStatusHistory> findByProductOrderByChangedAtDesc(Product product);
    
    List<ProductStatusHistory> findByStatus(ProductStatus status);
    
    @Query("SELECT psh FROM ProductStatusHistory psh WHERE psh.product = :product AND psh.status = :status ORDER BY psh.changedAt DESC")
    List<ProductStatusHistory> findByProductAndStatus(@Param("product") Product product, @Param("status") ProductStatus status);
    
    @Query("SELECT psh FROM ProductStatusHistory psh WHERE psh.changedAt BETWEEN :startDate AND :endDate")
    List<ProductStatusHistory> findByChangedAtBetween(@Param("startDate") LocalDateTime startDate, 
                                                    @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT psh FROM ProductStatusHistory psh WHERE psh.product = :product ORDER BY psh.changedAt DESC LIMIT 1")
    Optional<ProductStatusHistory> findLatestByProduct(@Param("product") Product product);
}