package org.assessment.the_drone.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assessment.the_drone.entity.Drone;
import org.assessment.the_drone.entity.Load;
import org.assessment.the_drone.exception.BadRequestException;
import org.assessment.the_drone.exception.ForbiddenException;
import org.assessment.the_drone.exception.NotFoundException;
import org.assessment.the_drone.model.State;
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
    public Drone registerDrone(Drone drone) throws Exception {
        // Assumption that serial number is unique
        Drone existedDrone = droneRepository.findBySerialNumber(drone.getSerialNumber());
        if(existedDrone != null) {
            throw new BadRequestException("Drone already exist with same serial number");
        }
        return droneRepository.save(drone);
    }
    
    @Override
    public Optional<Drone> findById(long droneId) {
        return droneRepository.findById(droneId);
    }

    @Override
    public Load loadDrone(long droneId, Load load) throws Exception {
        // Assumption that medication code is unique
        Load existedMedication = loadRepository.findByCode(load.getCode());
        if(existedMedication != null) {
            throw new BadRequestException("Medication already exist with same code");
        }
        Optional<Drone> drone = droneRepository.findById(droneId);
        if(!drone.isPresent()) {
            log.error("Exception - Drone does not exist");
            throw new NotFoundException("Drone does not exist");
        }
        if(drone.get().getBatteryCapacity() < 25) {
            log.error("Exception - Forbidden to load drone");
            if(drone.get().getState() == State.valueOf("LOADING")) {
                droneRepository.updatedroneState("IDLE", droneId);
            }
            throw new ForbiddenException("Drone battery capacity is below 25%");
        }
        Load loadedWeight = loadRepository.getDroneLoadedWeight(droneId);
        double totalWeight = loadedWeight != null ? loadedWeight.getWeight() + load.getWeight() : load.getWeight();
        if(totalWeight > drone.get().getWeightLimit()) {
            log.error("Exception - Forbidden to load drone");
            throw new ForbiddenException("Drone has reached maximum weight limit");
        }
        if (!drone.get().getState().equals(State.valueOf("LOADING")) 
                && !drone.get().getState().equals(State.valueOf("IDLE"))) {
            throw new ForbiddenException("Drone is not in a loadable state");
        }
        load.setDrone(drone.get());
        if(drone.get().getState() != State.valueOf("LOADING")) {
            droneRepository.updatedroneState("LOADING", droneId);
        }
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
        if(!drone.isPresent()) {
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
        List<Load> loadedItems = loadRepository.findByDroneId(droneId);
        if(loadedItems.isEmpty()) {
            throw new NotFoundException("Drone has not been loaded");
        }
        droneRepository.updatedroneState("DELIVERING", droneId);
        //Dispatch drone here
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR_OF_DAY, 3); // 3 hour delivery time
        SimpleDateFormat dateFormatter = new SimpleDateFormat("E dd-MMM-yyyy HH:mm:ss");
        return Map.of(
                "status", "Success",
                "dispatchTime", dateFormatter.format(new Date()),
                "expectedDeliveryTime", dateFormatter.format(calendar.getTime()),
                "trackingId", "ME-" + new Date().getTime() +"-LE684"
        );
    }

    @Override
    public void logDronesBatteryLevel() {
        List<Drone> drones = droneRepository.findAll();
        drones.forEach(drone -> {
            if(drone.getBatteryCapacity() < 25 && drone.getState().equals(State.valueOf("LOADING"))) {
                droneRepository.updatedroneState("IDLE", drone.getId());
            }
            log.info("The battery capacity for {} drone with serial number - {} is {}", 
                    drone.getModel(), drone.getSerialNumber(), drone.getBatteryCapacity());
        });
    }

}
