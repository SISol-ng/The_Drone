package org.assessment.the_drone.repository;

import java.util.List;
import org.assessment.the_drone.entity.Load;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author Muhammed.Ibrahim
 */
public interface LoadRepository extends JpaRepository<Load, Long> {

    @Query(value = "SELECT SUM(weight) weight, droneId FROM loads WHERE droneId = ?1", nativeQuery = true)
    Load getDroneLoadedWeight(long droneId);
    
    @Query(value = "SELECT id, droneId, name, weight, code, image FROM loads WHERE droneId = ?1 AND code = ?2", nativeQuery = true)
    Load findByDroneIdAndCode(long droneId, String code);
    
    List<Load> findByDroneId(long droneId);

}
