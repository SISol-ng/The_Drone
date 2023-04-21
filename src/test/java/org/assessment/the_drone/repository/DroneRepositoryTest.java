package org.assessment.the_drone.repository;

import com.github.database.rider.core.api.connection.ConnectionHolder;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.DBUnitExtension;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.assessment.the_drone.entity.Drone;
import org.assessment.the_drone.entity.Load;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 *
 * @author Muhammed.Ibrahim
 */
@ExtendWith({DBUnitExtension.class, SpringExtension.class})
@SpringBootTest
@ActiveProfiles("test")
@Slf4j
public class DroneRepositoryTest {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private DroneRepository repository;
    @Autowired
    private LoadRepository loadRepository;
    
    public ConnectionHolder getConnectionHolder() {
        // Return a function that retrieves a connection from our data source
        return () -> dataSource.getConnection();
    }
    
    @Test
    @DataSet("drone.yml")
    void testRegisterDrone() {
        // Create a new Drone and save it to the database
        Drone drone = new Drone("L2B16438200041G7", "Lightweight", 250.50, 97, "IDLE");
        Drone savedDrone = repository.save(drone);
        log.info("::::: Testing repository save: savedDrone => {}", savedDrone);
        
        // Validate the registered drone
        Assertions.assertEquals("L2B16438200041G7", savedDrone.getSerialNumber(), "Drone serial number should be same");
        Assertions.assertEquals("Lightweight", savedDrone.getModel(), "Drone model should match");
        Assertions.assertEquals(250.50, savedDrone.getWeightLimit.doubleValue(), "Drone weight limit should be the same");
        Assertions.assertEquals(97, savedDrone.getBatteryCapacity.intValue(), "Drone battery capacity should be the same");
        Assertions.assertEquals("IDLE", savedDrone.getState(), "Drone should be in \"IDLE\" state");
        
        // Validate that we can get it back out of the database
        Optional<Drone> retrievedDrone = repository.findById(savedDrone.getId());
        log.info("::::: Testing repository save: retrievedDrone => {}", retrievedDrone);
        Assertions.assertTrue(retrievedDrone.isPresent(), "Could retrieve registered drone from the database");
        Assertions.assertEquals("L2B16438200041G7", retrievedDrone.get().getSerialNumber(), "Retrieved Drone serial number should be same");
        Assertions.assertEquals("Lightweight", retrievedDrone.get().getModel(), "Correct Drone model");
        Assertions.assertEquals(250.50, retrievedDrone.get().getWeightLimit().doubleValue(), "Retrieved Drone weight limit should be the same");
        Assertions.assertEquals(97, retrievedDrone.get().getBatteryCapacity().intValue(), "Retrieved Drone battery capacity should be the same");
        Assertions.assertEquals("IDLE", retrievedDrone.get().getState(), "Retrieved Drone state should be \"IDLE\"");
    }
    
    @Test
    @DataSet("loads.yml")
    void testLoadDrone() {
        // Create a new Drone and save it to the database
        Load load = new Load(17, "New Medication", 25.00, "NEW_MED", "Base64_Image_String_New");
        Load loadedMedication = loadRepository.save(load);
        log.info("::::: Testing repository save: loadedMedication => {}", loadedMedication);
        
        // Validate the registered drone
        Assertions.assertEquals(17, loadedMedication.getDroneId.intValue(), "Drone Id should be the same");
        Assertions.assertEquals("New Medication", loadedMedication.getName(), Medication name should be same");
        Assertions.assertEquals(25.00, loadedMedication.getWeight.doubleValue(), "Drone weight limit should be the same");
        Assertions.assertEquals("NEW_MED", loadedMedication.getCode(), "Medication code should be the same");
        Assertions.assertEquals("Base64_Image_String_New", loadedMedication.getImage(), "Medication image should be the same");
        
        // Validate that we can get it back out of the database
        Optional<Drone> loadedDrone = loadRepository.findById(loadedMedication.getId());
        log.info("::::: Testing repository save: loadedDrone => {}", loadedDrone);
        Assertions.assertTrue(loadedDrone.isPresent(), "Could retrieve loaded drone from the database");
        Assertions.assertEquals("17", loadedDrone.get().getDroneId().intValue(), "Retrieved Drone Id should be same");
        Assertions.assertEquals("New Medication", loadedDrone.get().getName(), "Retrieved Medication name should match");
        Assertions.assertEquals(25.00, loadedDrone.get().getWeight().doubleValue(), "Retrieved Load weight should be the same");
        Assertions.assertEquals("NEW_MED", loadedDrone.get().getCode(), "Retrieved Medication code should be the same");
        Assertions.assertEquals("Base64_Image_String_New", loadedDrone.get().getImage(), "Retrieved Medication image should be the same");
    }
    
    @Test
    @DataSet("loads.yml")
    void testFindLoadedMedicationSuccess() {
        // Find the medication with code 'MED_81C' loaded in Drone with ID 12
        Optional<Load> medication = loadRepository.findByDroneIdAndCode(12, "MED_81C");
        log.info("::::: Testing repository testFindLoadedMedicationSuccess => {}", medication);
        //Validate that we found it
        Assertions.assertTrue(medication.isPresent(), "We should find a medication with ID code \"MED_81C\" loaded in drone with Id 12");

        Load med = medication.get();
        Assertions.assertEquals(12, med.getDroneId().intValue(), "The Drone ID should be 12");
        Assertions.assertEquals("Protophone BP", med.getName(), "Medication name should be \"Protophone BP\"");
        Assertions.assertEquals(150.0, med.getWeight().doubleValue(), "Load weight should match");
        Assertions.assertEquals("MED_81C", med.getCode(), "Medication code should be the same");
        Assertions.assertEquals("Base64_Image_String", med.getImage(), "Medication image should be the same");
    }
    
    @Test
    @DataSet("loads.yml")
    void testFindLoadedMedicationNotFound() {
        Optional<Load> medication = loadRepository.findByDroneIdAndCode(12, "MED_201");
        log.info("::::: Testing repository testFindLoadedMedicationNotFound => {}", medication);
        Assertions.assertFalse(medication.isPresent(), "Medication with Code \"MED_201\" should not be found in Drone with Id 12");
    }
    
    @Test
    @DataSet("drones.yml")
    void testFindDronesAvailableForLoading() {
        List<Drone> drones = repository.findDronesAvailableForLoading();
        log.info("::::: Testing repository testDronesInLoading => {}", drones);
        Assertions.assertEquals(2, drones.size(), "Expected 2 drones in loading state");
    }
    
    @Test
    @DataSet("drones.yml")
    void testFindDroneBySerialNumber() {
        // Find the drone with serial number 'L7A39438200041G9'
        Optional<Drone> drone = repository.findBySerialNumber("L7A39438200041G9");
        log.info("::::: Testing repository testFindDroneBySerialNumber => {}", drone);
        //Validate that we found it
        Assertions.assertTrue(drone.isPresent(), "We should find a drone with serial number \"L7A39438200041G9\"");

        Drone d = drone.get();
        Assertions.assertEquals(14, d.getId().intValue(), "The drone ID should be 14");
        Assertions.assertEquals("L7A39438200041G9", d.getSerialNumber(), "The drone serial number should be \"L7A39438200041G9\"");
        Assertions.assertEquals("Cruiserweight", d.getModel(), "Drone model should be \"Cruiserweight\"");
        Assertions.assertEquals(450.0, d.getWeightLimit().doubleValue(), "Drone Weight limit should be 450");
        Assertions.assertEquals(100, d.getBatteryCapacity().intValue(), "Drone battery capacity should be the 100%");
        Assertions.assertEquals("DELIVERING", d.getState(), "Drone should be in DELIVERING state");
    }
    
    @Test
    @DataSet("drones.yml")
    void testFindDroneById() {
        // Find the drone with id 13
        Optional<Drone> drone = repository.findById(13);
        log.info("::::: Testing repository testFindDroneById => {}", drone);
        //Validate that we found it
        Assertions.assertTrue(drone.isPresent(), "We should find a drone with ID 13");

        Drone d = drone.get();
        Assertions.assertEquals(13, d.getId().intValue(), "The drone ID should be 13");
        Assertions.assertEquals("A7B1643823304197", d.getSerialNumber(), "The drone serial number should be \"A7B1643823304197\"");
        Assertions.assertEquals("Heavyweight", d.getModel(), "Drone model should be \"Heavyweight\"");
        Assertions.assertEquals(500.0, d.getWeightLimit().doubleValue(), "Drone Weight limit should be 500");
        Assertions.assertEquals(24, d.getBatteryCapacity().intValue(), "Drone battery capacity should be the 24%");
        Assertions.assertEquals("IDLE", d.getState(), "Drone should be in IDLE state");
    }
}
