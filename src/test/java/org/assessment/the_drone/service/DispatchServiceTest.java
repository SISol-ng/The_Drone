package org.assessment.the_drone.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.assessment.the_drone.entity.Drone;
import org.assessment.the_drone.entity.Load;
import org.assessment.the_drone.exception.ForbiddenException;
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
    void testRegister() throws Exception {
        //Setup mocked drone
        Drone mockDrone = new Drone(5, "Heavyweight", "YYB16438200041F7", 480.0, 100, "IDLE");
        //Setup mocked repository
        doReturn(mockDrone).when(droneRepository).save(any());
        // Execute the service call
        Drone registeredDrone = service.registerDrone(mockDrone);
        log.info("::::: Testing Service save => {}", registeredDrone);
        // Assert the response
        Assertions.assertNotNull(registeredDrone, "The registered drone should not be null");
    }
    
    @Test
    @DisplayName("Test load drone - Success")
    void testLoadDroneSuccess() throws Exception {
        //Setup mocked drone
        Load postMedication = new Load("New Medication", 250.0, "NEW_MED", "Base64_Image_String_New");
        postMedication.setDrone(new Drone(7));
        Load mockLoad = new Load(1, 7, "New Medication", 250.0, "NEW_MED", "Base64_Image_String_New");
        Drone mockDrone = new Drone(7, "Heavyweight", "YYB16438200041F7", 480.0, 100, "LOADING");
        //Setup mocked service
        doReturn(Optional.of(mockDrone)).when(droneRepository).findById(7L);
        doReturn(mockLoad).when(loadRepository).save(any());
        // Execute the service call
        Load loadedMedication = service.loadDrone(7, postMedication);
        // Assert the response
        Assertions.assertEquals(loadedMedication, mockLoad, "Loaded medication should be returned");
    }
    
    @Test
    @DisplayName("Test load drone - Failed")
    void testLoadDroneStateFailed() throws Exception {
        //Setup mocked drone
        Load postMedication = new Load("New Medication", 250.0, "NEW_MED", "Base64_Image_String_New");
        postMedication.setDrone(new Drone(7));
        Load mockLoad = new Load(1, 7, "New Medication", 250.0, "NEW_MED", "Base64_Image_String_New");
        Drone mockDrone = new Drone(7, "Heavyweight", "YYB16438200041F7", 480.0, 100, "LOADED");
        //Setup mocked service
        doReturn(Optional.of(mockDrone)).when(droneRepository).findById(7L);
        doReturn(mockLoad).when(loadRepository).save(any());
        // Assert Exception
        Exception exception = Assertions.assertThrows(ForbiddenException.class, () -> {
            service.loadDrone(7, postMedication);
        });
        Assertions.assertTrue(exception.getMessage().contains("Drone is not in a loadable state"));
    }
    
    @Test
    @DisplayName("Test load drone - Failed")
    void testLoadDroneLowBatteryLevel() throws Exception {
        //Setup mocked drone
        Load postMedication = new Load("New Medication", 250.0, "NEW_MED", "Base64_Image_String_New");
        postMedication.setDrone(new Drone(7));
        Load mockLoad = new Load(1, 7, "New Medication", 250.0, "NEW_MED", "Base64_Image_String_New");
        Drone mockDrone = new Drone(7, "Heavyweight", "YYB16438200041F7", 480.0, 21, "LOADING");
        //Setup mocked service
        doReturn(Optional.of(mockDrone)).when(droneRepository).findById(7L);
        doReturn(mockLoad).when(loadRepository).save(any());
        // Assert Exception
        Exception exception = Assertions.assertThrows(ForbiddenException.class, () -> {
            service.loadDrone(7, postMedication);
        });
        Assertions.assertTrue(exception.getMessage().contains("Drone battery capacity is below 25%"));
    }
    
    @Test
    @DisplayName("Test load drone - Failed")
    void testLoadDroneWeightLimit() throws Exception {
        //Setup mocked drone
        Load postMedication = new Load("New Medication", 450.0, "NEW_MED", "Base64_Image_String_New");
        postMedication.setDrone(new Drone(7));
        Load mockLoad = new Load(1, 7, "New Medication", 450.0, "NEW_MED", "Base64_Image_String_New");
        Drone mockDrone = new Drone(7, "Heavyweight", "YYB16438200041F7", 440.0, 100, "LOADING");
        //Setup mocked service
        doReturn(Optional.of(mockDrone)).when(droneRepository).findById(7L);
        doReturn(mockLoad).when(loadRepository).save(any());
        // Assert Exception
        Exception exception = Assertions.assertThrows(ForbiddenException.class, () -> {
            service.loadDrone(7, postMedication);
        });
        Assertions.assertTrue(exception.getMessage().contains("Drone has reached maximum weight limit"));
    }
    
    @Test
    @DisplayName("Test findLoadedMedication Not Found")
    void testFindLoadedMedicationNotFound() {
        //Setup mocked drone
        Drone mockDrone = new Drone(12, "Heavyweight", "YYB16438200041F7", 480.0, 100, "LOADED");
        //Setup mocked repository
        doReturn(Optional.of(mockDrone)).when(droneRepository).findById(12L);
        doReturn(null).when(loadRepository).findByDroneIdAndCode(12, "MED_81C");
        // Execute the service call
        Load returnedLoad = service.getLoadedMedication(12, "MED_81C");
        log.info("::::: Testing Service findLoadedMedication => {}", returnedLoad);
        // Assert the response
        Assertions.assertNull(returnedLoad, "Medication should be found");
    }
    
    @Test
    @DisplayName("Test findLoadedMedication Success")
    void testFindLoadedMedication() {
        //Setup mocked drone and load
        Load mockLoad = new Load(4, 12, "Protophone BP", 150.0, "MED_81C", "Base64_Image_String");
        Drone mockDrone = new Drone(12, "Heavyweight", "YYB16438200041F7", 480.0, 100, "LOADED");
        //Setup mocked repository
        doReturn(Optional.of(mockDrone)).when(droneRepository).findById(12L);
        doReturn(mockLoad).when(loadRepository).findByDroneIdAndCode(12, "MED_81C");
        // Execute the service call
        Load returnedLoad = service.getLoadedMedication(12, "MED_81C");
        log.info("::::: Testing Service findLoadedMedication => {}", returnedLoad);
        // Assert the response
        Assertions.assertNotNull(returnedLoad, "Medication found");
        Assertions.assertSame(returnedLoad, mockLoad, "Load should be the same");
    }
    
    @Test
    @DisplayName("Test find all available drones for loading")
    void testFindDronesAvailableForLoading() {
        //Setup mocked drones
        Drone drone1 = new Drone(11, "Heavyweight", "L2B16438200041G7", 480.0, 87, "LOADING");
        Drone drone2 = new Drone(12, "Lightweight", "B1B16438202041A7", 180.0, 95, "IDLE");
        //Setup mocked repository
        doReturn(Arrays.asList(drone1, drone2)).when(droneRepository).findAvailableDrones();
        // Execute the service call
        List<Drone> drones = service.getAvailableDroneForLoading();
        log.info("::::: Testing Service find Available drones => {}", drones);
        // Assert the response
        Assertions.assertEquals(2, drones.size(), "findAvailableDrones should return 2 drones");
    }
    
    @Test
    @DisplayName("Test findById Success")
    void testFindByIdSuccess() {
        //Setup mocked drone
        Drone mockDrone = new Drone(11, "Heavyweight", "L2B16438200041G7", 480.0, 87, "LOADING");
        //Setup mocked repository
        doReturn(Optional.of(mockDrone)).when(droneRepository).findById(11L);
        // Execute the service call
        Optional<Drone> registeredDrone = service.findById(11);
        log.info("::::: Testing Service findById => {}", registeredDrone);
        // Assert the response
        Assertions.assertTrue(registeredDrone.isPresent(), "Drone found");
        Assertions.assertSame(registeredDrone.get(), mockDrone, "Drones should be the same");
    }
}
