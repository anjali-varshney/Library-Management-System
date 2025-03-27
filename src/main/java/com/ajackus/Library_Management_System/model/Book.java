package com.ajackus.Library_Management_System.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    private  String title;
    private  String author;
    private  String genre;
    private  String availabilityStatus;

    @Column(unique = true, nullable = false)
    private String isbn; //International Standard Book Number
}
