package org.assessment.the_drone.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Drone {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    @Enumerated(EnumType.STRING)
    @ValidDroneModel
    private Model model;
    
    @Length(min=15, max=100, message="Serial number must be between 15 and 100 digits")
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
}
