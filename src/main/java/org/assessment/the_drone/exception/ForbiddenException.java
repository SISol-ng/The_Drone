package org.assessment.the_drone.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author Muhammed.Ibrahim
 */
@Data
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ForbiddenException extends Exception{
    public ForbiddenException(String message) {
        super(message);
    }
}
