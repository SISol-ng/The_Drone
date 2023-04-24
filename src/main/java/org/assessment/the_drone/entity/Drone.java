package org.assessment.the_drone.entity;

import org.assessment.the_drone.model.Model;
import org.assessment.the_drone.model.State;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.assessment.the_drone.util.validator.ValidDroneModel;
import org.assessment.the_drone.util.validator.ValidDroneState;
import org.hibernate.validator.constraints.Length;

/**
 *
 * @author Muhammed.Ibrahim
 */
@Entity
@Table(name = "drones")
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Drone {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    @ValidDroneModel(valid = {Model.Lightweight, Model.Middleweight, Model.Cruiserweight, Model.Heavyweight})
    @Enumerated(EnumType.STRING)
    private Model model;
    
    @Length(min=15, max=100, message="Serial number must be between 15 and 100 digits")
    @Column(name = "serialNumber", unique = true)
    private String serialNumber;
    
    @Min(value = 0, message = "Weight limit should not be less than 0")
    @Max(value = 500, message = "Weight limit should not be greater than 500")
    private double weightLimit;
    
    @Min(value = 0, message = "Battery capacity should not be less than 0")
    @Max(value = 100, message = "Battery capacity should not be greater than 100")
    private int batteryCapacity;
    
    @Enumerated(EnumType.STRING)
    @ValidDroneState
    private State state;
    
    public Drone (long id) {
        this.id = id;
    }
    
    public Drone (String model, String serialNumber, Double weightLimit, int batteryCapacity, String state) {
        this.model = Model.valueOf(model);
        this.serialNumber = serialNumber;
        this.weightLimit = weightLimit;
        this.batteryCapacity = batteryCapacity;
        this.state = State.valueOf(state);
    }
    
    public Drone (long id, String model, String serialNumber, Double weightLimit, int batteryCapacity, String state) {
        this.id = id;
        this.model= Model.valueOf(model);
        this.serialNumber = serialNumber;
        this.weightLimit = weightLimit;
        this.batteryCapacity = batteryCapacity;
        this.state = State.valueOf(state);
    }
}
