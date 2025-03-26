package com.ajackus.Library_Management_System.model;

import lombok.Data;

@Data
public class Book {
    private  Long id;
    private  String title;
    private  String author;
    private  String genre;
    private  String availabilityStatus;
}
