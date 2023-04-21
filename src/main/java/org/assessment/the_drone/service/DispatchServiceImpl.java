package org.assessment.the_drone.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assessment.the_drone.entity.Drone;
import org.assessment.the_drone.entity.Load;
import org.assessment.the_drone.exception.ForbiddenException;
import org.assessment.the_drone.exception.NotFoundException;
import org.assessment.the_drone.repository.DroneRepository;
import org.assessment.the_drone.repository.LoadRepository;
import org.springframework.stereotype.Service;

/**
 *
 * @author Muhammed.Ibrahim
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DispatchServiceImpl implements DispatchService {
    
    private final DroneRepository droneRepository;
    private final LoadRepository loadRepository;

    @Override
    public Drone registerDrone(Drone drone) {
        return droneRepository.save(drone);
    }

    @Override
    public Load loadDrone(long droneId, Load load) throws Exception {
        Optional<Drone> drone = droneRepository.findById(droneId);
        if(drone.get() == null) {
            log.error("Exception - Drone does not exist");
            throw new NotFoundException("Drone does not exist");
        }
        if(drone.get().getBatteryCapacity() < 25) {
            log.error("Exception - Forbidden to load drone");
            throw new ForbiddenException("Drone battery capacity is below 25%");
        }
        Load loadedWeight = loadRepository.getDroneLoadedWeight(droneId);
        if((loadedWeight.getWeight() + load.getWeight()) > drone.get().getWeightLimit()) {
            log.error("Exception - Forbidden to load drone");
            throw new ForbiddenException("Drone has reached maximum weight limit");
        }
        load.setDrone(drone.get());
        return loadRepository.save(load);
    }

    @Override
    public Load getLoadedMedication(long droneId, String code) {
        return loadRepository.findByDroneIdAndCode(droneId, code);
    }
    
    @Override
    public List<Load> getAllLoadedMedications(long droneId) {
        return loadRepository.findByDroneId(droneId);
    }

    @Override
    public List<Drone> getAvailableDroneForLoading() {
        return droneRepository.findAvailableDrones();
    }

    @Override
    public Map getDroneBatteryLevel(long droneId) throws Exception {
        Optional<Drone> drone = droneRepository.findById(droneId);
        if(drone.get() == null) {
            log.error("Exception - Drone does not exist");
            throw new NotFoundException("Drone does not exist");
        }
        return Map.of(
                "status", "Success",
                "droneId", droneId,
                "batteryLevel", drone.get().getBatteryCapacity(),
                "state", drone.get().getState()
        );
    }

    @Override
    public Map dispatchDrone(long droneId) throws Exception {
        Optional<Drone> drone = droneRepository.findById(droneId);
        if(drone.get() == null) {
            log.error("Exception - Drone does not exist");
            throw new NotFoundException("Drone does not exist");
        }
        droneRepository.updatedroneState("DELIVERING", droneId);
        //Dispatch drone here
        return Map.of(
                "status", "Success",
                "dispatchTime", "2023-04-21 02:40:15",
                "expectedDeliveryTime", "2023-04-21 08:40:15",
                "trackingId", "ME-1664495262-684"
        );
    }

    @Override
    public void logDronesBatteryLevel() {
        List<Drone> drones = droneRepository.findAll();
        drones.forEach(drone -> {
            log.info("The battery capacity for {} drone with serial number - {} is {}", 
                    drone.getModel(), drone.getSerialNumber(), drone.getBatteryCapacity());
        });
    }

}
