package org.assessment.the_drone.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.assessment.the_drone.entity.Drone;
import org.assessment.the_drone.entity.Load;
import org.assessment.the_drone.model.Medication;
import org.assessment.the_drone.repository.DroneRepository;
import org.assessment.the_drone.repository.LoadRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 *
 * @author Muhammed.Ibrahim
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Slf4j
public class DispatchServiceTest {
    @Autowired
    private DispatchService service;
    @MockBean
    private DroneRepository droneRepository;
    @MockBean
    private LoadRepository loadRepository;
    
    @Test
    @DisplayName("Test register drone")
    void testRegister() {
        //Setup mocked drone
        Drone mockDrone = new Drone(5, "YYB16438200041F7", "Heavyweight", 480.0, 100, "IDLE");
        //Setup mocked repository
        doReturn(mockDrone).when(droneRepository).save(any());
        // Execute the service call
        Drone registeredDrone = service.register(mockDrone);
        log.info("::::: Testing Service save => {}", registeredDrone);
        // Assert the response
        Assertions.assertNotNull(registeredDrone, "The registered drone should not be null");
    }
    
    @Test
    @DisplayName("Test load drone")
    void testLoadDroneSuccess() {
        //Setup mocked drone
        Medication postMedication = new Medication(7, "New Medication", 250.0, "NEW_MED", "Base64_Image_String_New");
        Load mockLoad = new Load(1, 7, "New Medication", 250.00, "NEW_MED", "Base64_Image_String_New");
        Drone mockDrone = new Drone(7, "YYB16438200041F7", "Heavyweight", 480.0, 100, "LOADING");
        //Setup mocked service
        doReturn(Optional.of(mockDrone)).when(droneRepository).findById(7);
        doReturn(Optional.of(mockLoad)).when(loadRepository).save(any());
        // Execute the service call
        Optional<Load> loadedMedication = service.load(postMedication);
        // Assert the response
        Assertions.assertEquals(loadedMedication.get(), mockLoad, "Loaded medication should be returned");
    }
    
    @Test
    @DisplayName("Test findLoadedMedication Success")
    void testFindLoadedMedication() {
        //Setup mocked drone and load
        Load mockLoad = new Load(4, 12, "Protophone BP", 150.0, "MED_81C", "Base64_Image_String");
        Drone mockDrone = new Drone(12, "YYB16438200041F7", "Heavyweight", 480.0, 100, "LOADED");
        //Setup mocked repository
        doReturn(Optional.of(mockDrone)).when(droneRepository).findById(12);
        doReturn(Optional.of(mockLoad)).when(loadRepository).findByDroneIdAndCode(12, "MED_81C");
        // Execute the service call
        Optional<Load> returnedLoad = service.findLoadedMedication(12, "MED_81C");
        log.info("::::: Testing Service findLoadedMedication => {}", returnedLoad);
        // Assert the response
        Assertions.assertTrue(returnedLoad.isPresent(), "Medication found");
        Assertions.assertSame(returnedLoad.get(), mockLoad, "Load should be the same");
    }
    
    @Test
    @DisplayName("Test find all available drones for loading")
    void testFindDronesAvailableForLoading() {
        //Setup mocked drones
        Drone drone1 = new Drone(11, "L2B16438200041G7", "Heavyweight", 480.0, 87, "LOADING");
        Drone drone2 = new Drone(12, "B1B16438202041A7", "Lightweight", 180.0, 95, "IDLE");
        //Setup mocked repository
        doReturn(Arrays.asList(drone1, drone2)).when(droneRepository).findDronesAvailableForLoading();
        // Execute the service call
        List<Drone> drones = service.findDronesAvailableForLoading();
        log.info("::::: Testing Service find Available drones => {}", drones);
        // Assert the response
        Assertions.assertEquals(2, drones.size(), "findAvailableDrones should return 2 drones");
    }
    
    @Test
    @DisplayName("Test findById Success")
    void testFindById() {
        //Setup mocked drone
        Drone mockDrone = new Drone(11, "L2B16438200041G7", "Heavyweight", 480.0, 87, "LOADING");
        //Setup mocked repository
        doReturn(Optional.of(mockDrone)).when(droneRepository).findById(11);
        // Execute the service call
        Optional<Drone> registeredDrone = service.findById(1);
        log.info("::::: Testing Service findById => {}", registeredDrone);
        // Assert the response
        Assertions.assertTrue(registeredDrone.isPresent(), "Drone found");
        Assertions.assertSame(registeredDrone.get(), mockDrone, "Drones should be the same");
    }
}
