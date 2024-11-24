package nh.springgraphql.remoteservice.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ServiceExceptionHandler {

    @ExceptionHandler
    ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException rex) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(rex.getMessage());
    }

}
