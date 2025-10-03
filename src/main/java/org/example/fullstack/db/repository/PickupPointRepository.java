package org.example.fullstack.db.repository;

import org.example.fullstack.db.model.PickupPoint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PickupPointRepository extends JpaRepository<PickupPoint, Long> {
}
