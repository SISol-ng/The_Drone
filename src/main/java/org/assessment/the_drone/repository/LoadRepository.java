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

    @Query(value = "SELECT ?1 AS id, name, SUM(weight) weight, '' AS code, droneId, image FROM loads WHERE droneId = ?1 GROUP BY droneId", nativeQuery = true)
    Load getDroneLoadedWeight(long droneId);
    
    @Query(value = "SELECT id, droneId, name, weight, code, image FROM loads WHERE droneId = ?1 AND code = ?2", nativeQuery = true)
    Load findByDroneIdAndCode(long droneId, String code);
    
    List<Load> findByDroneId(long droneId);

    Load findByCode(String code);
}
