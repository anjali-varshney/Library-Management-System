package com.ajackus.Library_Management_System.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import lombok.extern.slf4j.Slf4j;



// Handles global exceptions for the Library Management System.
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


   // Handles LibraryManagementException (Book Not Found, Duplicate Book, etc.).
    @ExceptionHandler(LibraryManagementException.class)
    public ResponseEntity<String> handleLibraryManagementException(LibraryManagementException ex) {
        log.error("Library management error: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    //Handles any unexpected exceptions.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        log.error("Unexpected error occurred: {}", ex.getMessage());
        return new ResponseEntity<>("An unexpected error occurred. Please try again.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handles optimistic locking conflicts
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<String> handleOptimisticLockingFailure(ObjectOptimisticLockingFailureException ex) {
        log.warn("Optimistic locking failure: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Conflict: The book was updated or deleted by another transaction. Please try again.");
    }
}