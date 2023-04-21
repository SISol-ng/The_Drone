package org.assessment.the_drone.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.assessment.the_drone.util.validator.ValidMedicationCode;
import org.assessment.the_drone.util.validator.ValidMedicationName;

/**
 *
 * @author Muhammed.Ibrahim
 */
@Entity
@Table(name = "loads")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Load {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "droneId", nullable = false)
    private Drone drone;
    
    @NotNull(message = "Medication name cannot be null")
    @NotEmpty(message = "Medication name cannot be empty")
    @ValidMedicationName
    private String name;
    
    @Min(value = 0, message = "Weight should not be less than 0")
    @Max(value = 500, message = "Weight should not be greater than 500")
    private double weight;
    
    @NotNull(message = "Medication code cannot be null")
    @NotEmpty(message = "Medication code cannot be empty")
    @ValidMedicationCode
    private String code;
    
    private String image;
}
