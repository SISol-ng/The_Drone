package org.assessment.the_drone.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.assessment.the_drone.entity.Drone;
import org.assessment.the_drone.entity.Load;
import org.assessment.the_drone.service.DispatchService;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 *
 * @author Muhammed.Ibrahim
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DispatchControllerTest {
    @MockBean
    private DispatchService service;
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    @DisplayName("POST /drone/register - Success")
    void testRegisterDrone() throws Exception {
        //Setup mocked drone
        Drone postDrone = new Drone("YYB16438200041F7", "Heavyweight", 480.0, 100, "IDLE");
        Drone mockDrone = new Drone(5, "Heavyweight", "YYB16438200041F7", 480.0, 100, "IDLE");
        //Setup mocked service
        doReturn(mockDrone).when(service).registerDrone(any());
        
        // Execute the POST request
        mockMvc.perform(post("/drone/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(postDrone)))
                // Validate the response code and content type
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(5)))
                .andExpect(jsonPath("$.serialNumber", is("YYB16438200041F7")))
                .andExpect(jsonPath("$.model", is("Heavyweight")))
                .andExpect(jsonPath("$.weightLimit", is(480)))
                .andExpect(jsonPath("$.batteryCapacity", is(100)))
                .andExpect(jsonPath("$.state", is("IDLE")));
    }
    
    @Test
    @DisplayName("POST /drone/17/load - Success")
    void testLoadDrone() throws Exception {
        //Setup mocked drone
        Load postMedication = new Load("New Medication", 250.0, "NEW_MED", "Base64_Image_String_New");
        Load mockLoad = new Load(1, 17, "New Medication", 250.00, "NEW_MED", "Base64_Image_String_New");
        Drone mockDrone = new Drone(17, "Heavyweight", "YYB16438200041F7", 480.0, 100, "LOADING");
        //Setup mocked service
        doReturn(Optional.of(mockDrone)).when(service).findById(17);
        doReturn(mockLoad).when(service).loadDrone(17, postMedication);
        
        // Execute the POST request
        mockMvc.perform(post("/drone/{id}/load", 17)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(postMedication)))
                // Validate the response code and content type
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.droneId", is(17)))
                .andExpect(jsonPath("$.name", is("New Medication")))
                .andExpect(jsonPath("$.weight", is(25.0)))
                .andExpect(jsonPath("$.code", is("NEW_MED")))
                .andExpect(jsonPath("$.image", is("Base64_Image_String_New")));
    }
    
    @Test
    @DisplayName("POST /drone/12/load - Failed")
    void testLoadDroneWeightLimit() throws Exception {
        //Setup mocked drone
        Load postMedication = new Load("New Medication", 250.0, "NEW_MED", "Base64_Image_String_New");
        Load mockLoad = new Load(1, 12, "New Medication", 250.00, "NEW_MED", "Base64_Image_String_New");
        Drone mockDrone = new Drone(12, "Lightweight", "YYB16438200041F7", 180.0, 100, "LOADING");
        //Setup mocked service
        doReturn(Optional.of(mockDrone)).when(service).findById(12);
        doReturn(mockLoad).when(service).loadDrone(12, postMedication);
        
        // Execute the POST request
        mockMvc.perform(post("/drone/{id}/load", 12)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(postMedication)))
                // Validate the response code and content type
                .andExpect(status().isForbidden());
    }
    
    @Test
    @DisplayName("POST /drone/15/load - Failed")
    void testLoadDroneBatteryLevel() throws Exception {
        //Setup mocked drone
        Load postMedication = new Load("New Medication", 250.0, "NEW_MED", "Base64_Image_String_New");
        Load mockLoad = new Load(1, 15, "New Medication", 250.00, "NEW_MED", "Base64_Image_String_New");
        Drone mockDrone = new Drone(15, "Heavyweight", "YYB16438200041F7", 480.0, 24, "IDLE");
        //Setup mocked service
        doReturn(Optional.of(mockDrone)).when(service).findById(15);
        doReturn(mockLoad).when(service).loadDrone(15, postMedication);
        
        // Execute the POST request
        mockMvc.perform(post("/drone/{id}/load", 15)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(postMedication)))
                // Validate the response code and content type
                .andExpect(status().isForbidden());
    }
    
    @Test
    @DisplayName("POST /drone/15/load - Failed")
    void testLoadDroneState() throws Exception {
        //Setup mocked drone
        Load postMedication = new Load("New Medication", 250.0, "NEW_MED", "Base64_Image_String_New");
        Load mockLoad = new Load(1, 15, "New Medication", 250.00, "NEW_MED", "Base64_Image_String_New");
        Drone mockDrone = new Drone(15, "Heavyweight", "YYB16438200041F7", 480.0, 100, "LOADED");
        //Setup mocked service
        doReturn(Optional.of(mockDrone)).when(service).findById(15);
        doReturn(mockLoad).when(service).loadDrone(15, postMedication);
        
        // Execute the POST request
        mockMvc.perform(post("/drone/{id}/load", 15)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(postMedication)))
                // Validate the response code and content type
                .andExpect(status().isForbidden());
    }
    
    @Test
    @DisplayName("GET /drone/12/load/MED_81C - Found")
    void testFindLoadedMedicationFound() throws Exception {
        //Setup mocked load
        Load mockLoad = new Load(4, 12, "Protophone BP", 150.0, "MED_81C", "Base64_Image_String");
        Drone mockDrone = new Drone(12, "Heavyweight", "YYB16438200041F7", 480.0, 100, "LOADED");
        //Setup mocked service
        doReturn(Optional.of(mockDrone)).when(service).findById(12);
        doReturn(Optional.of(mockLoad)).when(service).getLoadedMedication(12, "MED_81C");
        
        //Execute the GET request
        mockMvc.perform(get("/drone/{id}/load/{code}", 12, "MED_81C"))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(4)))
                .andExpect(jsonPath("$.droneId", is(12)))
                .andExpect(jsonPath("$.name", is("Protophone BP")))
                .andExpect(jsonPath("$.weight", is(150.0)))
                .andExpect(jsonPath("$.code", is("MED_81C")))
                .andExpect(jsonPath("$.image", is("Base64_Image_String")));
    }
    
    @Test
    @DisplayName("GET /drone/12/load/MED_901 - Not Found")
    void testFindLoadedMedicationNotFound() throws Exception {
        //Setup mocked service
        doReturn(null).when(service).getLoadedMedication(12, "MED_901");
        
        // Execute the GET request
        mockMvc.perform(get("/drone/{id}/load/{code}", 12, "MED_901"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @DisplayName("GET /drone/available - Found")
    void testGetDronesAvailableForLoading() throws Exception {
        //Setup mocked load
        Drone drone1 = new Drone(11, "Heavyweight", "L2B16438200041G7", 480.0, 87, "LOADING");
        Drone drone2 = new Drone(12, "Lightweight", "B1B16438202041A7", 180.0, 95, "IDLE");
        List<Drone> drones = new ArrayList<>(Arrays.asList(drone1, drone2));
        //Setup mocked service
        doReturn(drones).when(service).getAvailableDroneForLoading();
        
        //Execute the GET request
        mockMvc.perform(get("/drone/available"))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$.total", is(2)))
                .andExpect(jsonPath("$.drones").isArray())
                .andExpect(jsonPath("$.drones").isNotEmpty())
                .andExpect(jsonPath("$.drones", hasSize(2)));
    }
    
    @Test
    @DisplayName("POST /drone/11/dispatch - Success")
    void testDispatchDrone() throws Exception {
        //Setup mocked drone
        Drone mockDrone = new Drone(11, "Lightweight", "YYB16438200041F7", 180.0, 100, "LOADED");
        //Setup mocked service
        doReturn(Optional.of(mockDrone)).when(service).findById(12);
        doReturn(null).when(service).dispatchDrone(any());
        
        // Execute the POST request
        mockMvc.perform(post("/drone/{id}/register", 11))
                // Validate the response code and content type
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$.status", is("Success")))
                .andExpect(jsonPath("$.dispatchTime", is("2023-04-21 02:40:15")))
                .andExpect(jsonPath("$.expectedDeliveryTime", is("2023-04-21 08:40:15")))
                .andExpect(jsonPath("$.trackingId", is("ME-1664495262-684")));
    }
    
    @Test
    @DisplayName("GET /drone/21/battery - Success")
    void testGetDroneBatteryLevelSuccess() throws Exception {
        //Setup mocked load
        Drone mockDrone = new Drone(21, "Lightweight", "YYB16438200041F7", 180.0, 57, "IDLE");
        //Setup mocked service
        doReturn(Optional.of(mockDrone)).when(service).findById(21);
        
        //Execute the GET request
        mockMvc.perform(get("/drone/{id}/battery", 21))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$.status", is("Success")))
                .andExpect(jsonPath("$.droneId", is(21)))
                .andExpect(jsonPath("$.batteryLevel", is(57)))
                .andExpect(jsonPath("$.state", is("IDLE")));
    }
    
    @Test
    @DisplayName("GET /drone/1/battery - Not Found")
    void testGetDroneBatteryLevelNotFound() throws Exception {
        //Setup mocked service
        doReturn(Optional.empty()).when(service).findById(1);
        
        //Execute the GET request
        mockMvc.perform(get("/drone/{id}/battery", 1))
                .andExpect(status().isNotFound());
    }
    
    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
