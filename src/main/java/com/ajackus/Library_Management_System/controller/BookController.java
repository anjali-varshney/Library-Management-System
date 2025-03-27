package com.ajackus.Library_Management_System.controller;

import com.ajackus.Library_Management_System.dto.BookDTO;
import com.ajackus.Library_Management_System.exception.LibraryManagementException;
import com.ajackus.Library_Management_System.service.interfaces.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

//@Controller
@RestController
@RequestMapping("api/v1/books")
@Slf4j
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping("/add")
    public ResponseEntity<?> addBook(@RequestBody BookDTO bookDTO) {
        log.info("Received request to add book: {}", bookDTO);

        try {
            BookDTO savedBook = bookService.addBook(bookDTO);
            log.info("Book added successfully with ID: {}", savedBook.getId());
            return ResponseEntity.ok(savedBook);
        } catch (LibraryManagementException e) {
            log.error("Error adding book: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        }
    }


    // Get all books
    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        log.info("Fetching all books...");
        List<BookDTO> books = bookService.getAllBooks();
        log.info("Total books found: {}", books.size());
        return ResponseEntity.ok(books);
    }

    // Get book by ID
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable String isbn) {
        log.info("Fetching book with ID: {}", isbn);
        BookDTO book = bookService.getBookByISBN(isbn);
        log.info("Book found: {}", book);
        return ResponseEntity.ok(book);
    }

    // Search book by title
    //We are assuming there is no duplacy in the title of books.
    @GetMapping("/search")
    public ResponseEntity<BookDTO> searchByTitle(@RequestParam String title) {
        log.info("Searching books with title containing: '{}'", title);
        BookDTO book = bookService.getBookByTitle(title);
        log.info("Books found: {}", book);
        return ResponseEntity.ok(book);
    }

    // Update book details
    @PutMapping("/update/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable String isbn, @RequestBody BookDTO bookDTO) {
        log.info("Received request to update book ID: {}", isbn);
        BookDTO updatedBook = bookService.updateBook(isbn, bookDTO);
        log.info("Book updated successfully: {}", updatedBook);
        return ResponseEntity.ok(updatedBook);
    }

    // Delete book by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable String isbn) {
        log.info("Received request to delete book ID: {}", isbn);
        bookService.deleteBook(isbn);
        log.info("Book deleted successfully.");
        return ResponseEntity.ok("Book deleted successfully");
    }

}
