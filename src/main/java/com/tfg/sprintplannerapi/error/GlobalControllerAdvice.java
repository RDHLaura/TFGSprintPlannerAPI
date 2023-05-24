package com.tfg.sprintplannerapi.error;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLException;

@RestControllerAdvice
public class GlobalControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDTO> handleNotFound(NotFoundException exception){
        ErrorDTO apiError = new ErrorDTO(HttpStatus.NOT_FOUND, exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    @ExceptionHandler(MappingException.class)
    public ResponseEntity<ErrorDTO> handleNotFound(MappingException exception){
        ErrorDTO apiError = new ErrorDTO(HttpStatus.BAD_REQUEST, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(BadInputException.class)
    public ResponseEntity<ErrorDTO> handleBadInput(BadInputException exception){
        ErrorDTO apiError = new ErrorDTO(HttpStatus.BAD_REQUEST, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }
    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ErrorDTO> handleUnauthorizedAccess(UnauthorizedAccessException exception){
        ErrorDTO apiError = new ErrorDTO(HttpStatus.FORBIDDEN, exception.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiError);
    }
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Object> handleSQLError(SQLException exception, WebRequest request){
        ErrorDTO errorDTO = new ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
    }
    @Override
    public ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                          HttpStatus statusCode, WebRequest request){
        ErrorDTO errorDTO = new ErrorDTO(statusCode, ex.getMessage());
        return ResponseEntity.status(statusCode).body(errorDTO);
    }
}
