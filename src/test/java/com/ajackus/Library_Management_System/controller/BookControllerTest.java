package com.ajackus.Library_Management_System.controller;

import com.ajackus.Library_Management_System.dto.BookDTO;
import com.ajackus.Library_Management_System.service.interfaces.BookService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private BookDTO sampleBook;

    @Before
    public void setUp() {
        sampleBook = new BookDTO();
        sampleBook.setIsbn("1234567890");
        sampleBook.setTitle("Test Book");
        sampleBook.setAuthor("John Doe");
        sampleBook.setGenre("Fiction");
        sampleBook.setAvailabilityStatus("Available");
    }

    @Test
    public void testAddBook() {
        when(bookService.addBook(any(BookDTO.class))).thenReturn(sampleBook);

        ResponseEntity<?> response = bookController.addBook(sampleBook);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(sampleBook, response.getBody());
    }

    @Test
    public void testGetAllBooks() {
        List<BookDTO> books = Arrays.asList(sampleBook);
        when(bookService.getAllBooks()).thenReturn(books);

        ResponseEntity<List<BookDTO>> response = bookController.getAllBooks();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    public void testGetBookById() {
        when(bookService.getBookByISBN("1234567890")).thenReturn(sampleBook);

        ResponseEntity<BookDTO> response = bookController.getBookById("1234567890");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(sampleBook, response.getBody());
    }

    @Test
    public void testSearchByTitle() {
        when(bookService.getBookByTitle("Test Book")).thenReturn(sampleBook);

        ResponseEntity<BookDTO> response = bookController.searchByTitle("Test Book");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(sampleBook, response.getBody());
    }

    @Test
    public void testUpdateBook() {
        when(bookService.updateBook(eq("1234567890"), any(BookDTO.class))).thenReturn(sampleBook);

        ResponseEntity<BookDTO> response = bookController.updateBook("1234567890", sampleBook);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(sampleBook, response.getBody());
    }

    @Test
    public void testDeleteBook() {
        doNothing().when(bookService).deleteBook("1234567890");

        ResponseEntity<String> response = bookController.deleteBook("1234567890");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Book deleted successfully", response.getBody());
    }
}
