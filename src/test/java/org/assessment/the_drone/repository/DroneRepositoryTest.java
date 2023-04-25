package org.assessment.the_drone.repository;

import lombok.extern.slf4j.Slf4j;
import org.assessment.the_drone.entity.Drone;
import org.assessment.the_drone.entity.Load;
import org.assessment.the_drone.model.Model;
import org.assessment.the_drone.model.State;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.DBUnitExtension;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;

/**
 *
 * @author Muhammed.Ibrahim
 */
@ExtendWith({DBUnitExtension.class, SpringExtension.class})
@SpringBootTest
@Slf4j
public class DroneRepositoryTest {
    @Autowired
    private DroneRepository repository;
    @Autowired
    private LoadRepository loadRepository;
    
    @Test
    @DataSet("drones.yml, loads.yml")
    void testRegisterDrone() {
        // Create a new Drone and save it to the database
        Drone drone = new Drone("Lightweight", "L2B1643820218041G7", 250.50, 97, "IDLE");
        Drone savedDrone = repository.save(drone);
        log.info("::::: Testing repository save: savedDrone => {}", savedDrone);
        
        // Validate the registered drone
        Assertions.assertEquals("L2B1643820218041G7", savedDrone.getSerialNumber(), "Drone serial number should be same");
        Assertions.assertEquals(Model.valueOf("Lightweight"), savedDrone.getModel(), "Drone model should match");
        Assertions.assertEquals(250.50, savedDrone.getWeightLimit(), "Drone weight limit should be the same");
        Assertions.assertEquals(97, savedDrone.getBatteryCapacity(), "Drone battery capacity should be the same");
        Assertions.assertEquals(State.valueOf("IDLE"), savedDrone.getState(), "Drone should be in \"IDLE\" state");
        
        // Validate that we can get it back out of the database
        Optional<Drone> retrievedDrone = repository.findById(savedDrone.getId());
        log.info("::::: Testing repository save: retrievedDrone => {}", retrievedDrone.get());
        Assertions.assertTrue(retrievedDrone.isPresent(), "Could retrieve registered drone from the database");
        Assertions.assertEquals("L2B1643820218041G7", retrievedDrone.get().getSerialNumber(), "Retrieved Drone serial number should be same");
        Assertions.assertEquals(Model.valueOf("Lightweight"), retrievedDrone.get().getModel(), "Correct Drone model");
        Assertions.assertEquals(250.50, retrievedDrone.get().getWeightLimit(), "Retrieved Drone weight limit should be the same");
        Assertions.assertEquals(97, retrievedDrone.get().getBatteryCapacity(), "Retrieved Drone battery capacity should be the same");
        Assertions.assertEquals(State.valueOf("IDLE"), retrievedDrone.get().getState(), "Retrieved Drone state should be \"IDLE\"");
    }
    
    @Test
    @DataSet("loads.yml, drones.yml")
    void testLoadDrone() {
        // Create a new Drone and save it to the database
        Load load = new Load("New Medication", 25.00, "NEW_MED", "Base64_Image_String_New");
        load.setDrone(new Drone(13));
        Load loadedMedication = loadRepository.save(load);
        log.info("::::: Testing repository save: loadedMedication => {}", loadedMedication);
        
        // Validate the registered drone
        Assertions.assertEquals(13, loadedMedication.getDrone().getId(), "Drone Id should be the same");
        Assertions.assertEquals("New Medication", loadedMedication.getName(), "Medication name should be same");
        Assertions.assertEquals(25.00, loadedMedication.getWeight(), "Drone weight limit should be the same");
        Assertions.assertEquals("NEW_MED", loadedMedication.getCode(), "Medication code should be the same");
        Assertions.assertEquals("Base64_Image_String_New", loadedMedication.getImage(), "Medication image should be the same");
        
        // Validate that we can get it back out of the database
        Optional<Load> loadedDrone = loadRepository.findById(loadedMedication.getId());
        log.info("::::: Testing repository save: loadedDrone => {}", loadedDrone.get());
        Assertions.assertTrue(loadedDrone.isPresent(), "Could retrieve loaded drone from the database");
        Assertions.assertEquals(13, loadedDrone.get().getDrone().getId(), "Retrieved Drone Id should be same");
        Assertions.assertEquals("New Medication", loadedDrone.get().getName(), "Retrieved Medication name should match");
        Assertions.assertEquals(25.00, loadedDrone.get().getWeight(), "Retrieved Load weight should be the same");
        Assertions.assertEquals("NEW_MED", loadedDrone.get().getCode(), "Retrieved Medication code should be the same");
        Assertions.assertEquals("Base64_Image_String_New", loadedDrone.get().getImage(), "Retrieved Medication image should be the same");
    }
    
    @Test
    @DataSet("loads.yml, drones.yml")
    void testFindLoadedMedicationSuccess() {
        // Find the medication with code 'MED_81C' loaded in Drone with ID 12
        Load medication = loadRepository.findByDroneIdAndCode(12, "MED_81C");
        log.info("::::: Testing repository testFindLoadedMedicationSuccess => {}", medication);
        //Validate that we found it
        Assertions.assertNotNull(medication, "We should find a medication with ID code \"MED_81C\" loaded in drone with Id 12");

        Assertions.assertEquals(12, medication.getDrone().getId(), "The Drone ID should be 12");
        Assertions.assertEquals("Protophone BP", medication.getName(), "Medication name should be \"Protophone BP\"");
        Assertions.assertEquals(150.0, medication.getWeight(), "Load weight should match");
        Assertions.assertEquals("MED_81C", medication.getCode(), "Medication code should be the same");
        Assertions.assertEquals("Base64_Image_String", medication.getImage(), "Medication image should be the same");
    }
    
    @Test
    @DataSet("loads.yml, drones.yml")
    void testFindLoadedMedicationNotFound() {
        Load medication = loadRepository.findByDroneIdAndCode(12, "MED_201");
        log.info("::::: Testing repository testFindLoadedMedicationNotFound => {}", medication);
        Assertions.assertNull(medication, "Medication with Code \"MED_201\" should not be found in Drone with Id 12");
    }
    
    @Test
    @DataSet("drones.yml, loads.yml")
    void testFindDronesAvailableForLoading() {
        List<Drone> drones = repository.findAvailableDrones();
        log.info("::::: Testing repository testDronesInLoading => {}", drones);
        Assertions.assertEquals(2, drones.size(), "Expected 2 drones in loading state");
        Assertions.assertEquals("L2B16438200041G7", drones.get(0).getSerialNumber(), "Expected first drone serial number to be \"L2B16438200041G7\"");
        Assertions.assertEquals("B1B16438202041A7", drones.get(1).getSerialNumber(), "Expected second drone serial number to be \"B1B16438202041A7\"");
    }
    
    @Test
    @DataSet("drones.yml, loads.yml")
    void testFindDroneBySerialNumber() {
        // Find the drone with serial number 'L7A39438200041G9'
        Drone drone = repository.findBySerialNumber("L7A39438200041G9");
        log.info("::::: Testing repository testFindDroneBySerialNumber => {}", drone);
        //Validate that we found it
        Assertions.assertNotNull(drone, "We should find a drone with serial number \"L7A39438200041G9\"");

        Assertions.assertEquals(14, drone.getId(), "The drone ID should be 14");
        Assertions.assertEquals("L7A39438200041G9", drone.getSerialNumber(), "The drone serial number should be \"L7A39438200041G9\"");
        Assertions.assertEquals(Model.valueOf("Cruiserweight"), drone.getModel(), "Drone model should be \"Cruiserweight\"");
        Assertions.assertEquals(450.0, drone.getWeightLimit(), "Drone Weight limit should be 450");
        Assertions.assertEquals(100, drone.getBatteryCapacity(), "Drone battery capacity should be the 100%");
        Assertions.assertEquals(State.valueOf("DELIVERING"), drone.getState(), "Drone should be in DELIVERING state");
    }
    
    @Test
    @DataSet("drones.yml, loads.yml")
    void testFindDroneById() {
        // Find the drone with id 13
        Optional<Drone> drone = repository.findById(13L);
        log.info("::::: Testing repository testFindDroneById => {}", drone.get());
        //Validate that we found it
        Assertions.assertTrue(drone.isPresent(), "We should find a drone with ID 13");

        Drone d = drone.get();
        Assertions.assertEquals(13, d.getId(), "The drone ID should be 13");
        Assertions.assertEquals("A7B1643823304197", d.getSerialNumber(), "The drone serial number should be \"A7B1643823304197\"");
        Assertions.assertEquals(Model.valueOf("Heavyweight"), d.getModel(), "Drone model should be \"Heavyweight\"");
        Assertions.assertEquals(500.0, d.getWeightLimit(), "Drone Weight limit should be 500");
        Assertions.assertEquals(24, d.getBatteryCapacity(), "Drone battery capacity should be the 24%");
        Assertions.assertEquals(State.valueOf("IDLE"), d.getState(), "Drone should be in IDLE state");
    }
}
