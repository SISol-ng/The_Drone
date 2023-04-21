package org.assessment.the_drone.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.assessment.the_drone.exception.ForbiddenException;
import org.assessment.the_drone.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author Muhammed.Ibrahim
 */
@ControllerAdvice
@Slf4j
public class ExceptionController {
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ ConstraintViolationException.class })
    public ResponseEntity<?> validationExceptionHandler(ConstraintViolationException ex) {
        StringBuilder sb = new StringBuilder();
        log.info(ex.toString());
        try {
            ex.getConstraintViolations().forEach(error -> {
                String errorMessage = error.getMessage();
                sb.append(errorMessage).append("; ");
            });
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new ResponseEntity<>(Map.of("Exception", sb.toString()), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleForbiddenException(NotFoundException ex) throws JsonProcessingException {
        return new ResponseEntity<>(Map.of("Exception", ex.getMessage()),  HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<?> handleForbiddenException(ForbiddenException ex) throws JsonProcessingException {
        return new ResponseEntity<>(Map.of("Exception", ex.getMessage()),  HttpStatus.UNAUTHORIZED);
    }
}
