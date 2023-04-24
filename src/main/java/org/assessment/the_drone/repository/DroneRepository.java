package org.assessment.the_drone.repository;

import jakarta.transaction.Transactional;
import java.util.List;
import org.assessment.the_drone.entity.Drone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author Muhammed.Ibrahim
 */
public interface DroneRepository extends JpaRepository<Drone, Long> {
    
    @Query(value = "SELECT id, serialNumber, model, weightLimit, batteryCapacity, state FROM drones WHERE state IN ('IDLE','LOADING') AND batteryCapacity > 24", nativeQuery = true)
    List<Drone> findAvailableDrones();
    
    Drone findBySerialNumber(String serialNumber);
    
    @Modifying
    @Transactional
    @Query(value = "UPDATE drones SET state = ?1 WHERE id = ?2", nativeQuery = true)
    void updatedroneState(String state, long droneId);

}
