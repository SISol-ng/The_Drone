package org.assessment.the_drone.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.assessment.the_drone.entity.Drone;
import org.assessment.the_drone.entity.Load;

/**
 *
 * @author Muhammed.Ibrahim
 */
public interface DispatchService {

    public Drone registerDrone(Drone drone) throws Exception;
    
    public Optional<Drone> findById(long droneId);

    public Load loadDrone(long droneId, Load load) throws Exception;

    public Load getLoadedMedication(long droneId, String code);

    public List<Drone> getAvailableDroneForLoading();

    public Map getDroneBatteryLevel(long droneId) throws Exception;

    public Map dispatchDrone(long droneId) throws Exception;

    public void logDronesBatteryLevel();

    public List<Load> getAllLoadedMedications(long droneId);
    
}
