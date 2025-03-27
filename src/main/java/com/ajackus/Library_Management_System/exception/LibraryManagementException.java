package com.ajackus.Library_Management_System.exception;



// Custom exception for all book-related errors in the Library Management System.

public class LibraryManagementException extends RuntimeException {
    public LibraryManagementException(String message) {
        super(message);
    }
}
