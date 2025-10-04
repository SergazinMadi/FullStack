package org.example.fullstack.db.repository;

import org.example.fullstack.db.enums.PickupPointType;
import org.example.fullstack.db.model.City;
import org.example.fullstack.db.model.PickupPoint;
import org.example.fullstack.db.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PickupPointRepository extends JpaRepository<PickupPoint, Long> {
    List<PickupPoint> findByCity(City city);
    
    List<PickupPoint> findByCityAndIsActive(City city, Boolean isActive);
    
    List<PickupPoint> findByType(PickupPointType type);
    
    List<PickupPoint> findByManager(User manager);
    
    @Query("SELECT pp FROM PickupPoint pp WHERE pp.city = :city AND pp.isActive = true AND pp.currentLoad < pp.maxCapacity")
    List<PickupPoint> findAvailablePickupPointsInCity(@Param("city") City city);
    
    @Query("SELECT pp FROM PickupPoint pp WHERE pp.city = :city AND pp.isActive = true AND pp.hasColdStorage = true AND pp.currentLoad < pp.maxCapacity")
    List<PickupPoint> findAvailableColdStoragePickupPointsInCity(@Param("city") City city);
    
    @Query("SELECT pp FROM PickupPoint pp WHERE pp.isActive = true AND pp.currentLoad < pp.maxCapacity AND pp.maxPackageWeight >= :weight")
    List<PickupPoint> findAvailablePickupPointsForWeight(@Param("weight") Double weight);
    
    Optional<PickupPoint> findByNameAndCity(String name, City city);
}