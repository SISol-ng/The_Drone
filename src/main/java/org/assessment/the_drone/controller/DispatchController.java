package org.assessment.the_drone.controller;

import java.util.HashMap;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.assessment.the_drone.entity.Drone;
import org.assessment.the_drone.entity.Load;
import org.assessment.the_drone.service.DispatchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Muhammed.Ibrahim
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/drone")
@Validated
public class DispatchController {
    
    private final DispatchService service;
    
    @PostMapping("/register")
    public ResponseEntity<Drone> registerDrone(@RequestBody @Valid Drone drone) throws Exception {
        Drone newDrone = service.registerDrone(drone);
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(newDrone);
    }
    
    @PostMapping("/{droneId}/load")
    public ResponseEntity<Load> loadDrone(@PathVariable long droneId, @RequestBody @Valid Load load) throws Exception {
        Load newLoad = service.loadDrone(droneId, load);
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(newLoad);
    }
    
    @GetMapping("/{droneId}/load/{code}")
    public ResponseEntity<Load> getLoadedMedication(@PathVariable("droneId") long droneId, @PathVariable("code") String code) {
        Load newLoad = service.getLoadedMedication(droneId, code);
        return ResponseEntity.status(HttpStatus.OK.value()).body(newLoad);
    }
    
    @GetMapping("/{droneId}/items")
    public ResponseEntity<HashMap<String, Object>> getAllLoadedMedications(@PathVariable("droneId") long droneId) throws Exception {
        List<Load> loads = service.getAllLoadedMedications(droneId);
        HashMap<String, Object> map = new HashMap<>();
        map.put("items", loads);
        return ResponseEntity.status(HttpStatus.OK.value()).body(map);
    }
    
    @GetMapping("/available")
    public ResponseEntity<HashMap<String, Object>> getAvailableDroneForLoading() {
        List<Drone> drones = service.getAvailableDroneForLoading();
        HashMap<String, Object> map = new HashMap<>();
        map.put("total", drones.size());
        map.put("drones", drones);
        return ResponseEntity.status(HttpStatus.OK.value()).body(map);
    }
    
    @GetMapping("/{droneId}/battery")
    public ResponseEntity<Map> getDroneBatteryLevel(@PathVariable("droneId") long droneId) throws Exception {
        Map response = service.getDroneBatteryLevel(droneId);
        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }
    
    @PostMapping("/{droneId}/dispatch")
    public ResponseEntity<Map> dispatchDrone(@PathVariable("droneId") long droneId) throws Exception {
        Map response = service.dispatchDrone(droneId);
        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }
    
    //Runs every 5 minutes
    @Scheduled(cron = "*/5 * * * *")
    public void logDronesBatteryLevel() {
        service.logDronesBatteryLevel();
    }
}
