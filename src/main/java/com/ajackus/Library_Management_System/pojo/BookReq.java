package com.ajackus.Library_Management_System.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class BookReq {
    private  Long id;
    private  String title;
    private  String author;
    private  String genre;
    private  String availabilityStatus;
}
