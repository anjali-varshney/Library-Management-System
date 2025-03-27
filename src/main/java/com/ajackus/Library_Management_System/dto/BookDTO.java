package com.ajackus.Library_Management_System.dto;

import com.ajackus.Library_Management_System.model.Book;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {


    private  Long id;

    private  String title;
    private  String author;
    private  String genre;
    private  String availabilityStatus;
    private String isbn;

    public BookDTO(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.genre = book.getGenre();
        this.availabilityStatus = book.getAvailabilityStatus();
        this.isbn = book.getIsbn();
    }

}
