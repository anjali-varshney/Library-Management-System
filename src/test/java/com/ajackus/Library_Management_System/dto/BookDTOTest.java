package com.ajackus.Library_Management_System.dto;

import com.ajackus.Library_Management_System.model.Book;
import org.junit.Test;
import static org.junit.Assert.*;

public class BookDTOTest {

    @Test
    public void testNoArgsConstructor() {
        BookDTO bookDTO = new BookDTO();
        assertNull(bookDTO.getId());
        assertNull(bookDTO.getTitle());
        assertNull(bookDTO.getAuthor());
        assertNull(bookDTO.getGenre());
        assertNull(bookDTO.getAvailabilityStatus());
        assertNull(bookDTO.getIsbn());
    }

    @Test
    public void testAllArgsConstructor() {
        BookDTO bookDTO = new BookDTO(1L, "The Alchemist", "Paulo Coelho", "Fiction", "Available", "1234567890");

        assertEquals(Long.valueOf(1), bookDTO.getId());
        assertEquals("The Alchemist", bookDTO.getTitle());
        assertEquals("Paulo Coelho", bookDTO.getAuthor());
        assertEquals("Fiction", bookDTO.getGenre());
        assertEquals("Available", bookDTO.getAvailabilityStatus());
        assertEquals("1234567890", bookDTO.getIsbn());
    }

    @Test
    public void testBookConstructor() {
        Book book = new Book();
        book.setId(2L);
        book.setTitle("1984");
        book.setAuthor("George Orwell");
        book.setGenre("Dystopian");
        book.setAvailabilityStatus("Checked Out");
        book.setIsbn("9876543210");

        BookDTO bookDTO = new BookDTO(book);

        assertEquals(Long.valueOf(2), bookDTO.getId());
        assertEquals("1984", bookDTO.getTitle());
        assertEquals("George Orwell", bookDTO.getAuthor());
        assertEquals("Dystopian", bookDTO.getGenre());
        assertEquals("Checked Out", bookDTO.getAvailabilityStatus());
        assertEquals("9876543210", bookDTO.getIsbn());
    }
}
