package com.ajackus.Library_Management_System.repository;

import com.ajackus.Library_Management_System.model.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // Use MySQL instead of H2
@TestPropertySource(locations = "classpath:application-test.properties") // Load test properties
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void shouldFindBookByTitle() {
        // Arrange: Create and save a book
        Book book = new Book();
        book.setTitle("Test Book");
        book.setAuthor("John Doe");
        book.setGenre("Fiction");
        book.setAvailabilityStatus("Available");
        book.setIsbn("1234567890");
        bookRepository.save(book);

        // Act: Retrieve book by title
        Optional<Book> foundBook = bookRepository.findByTitle("Test Book");

        // Assert: Book should be present
        assertTrue(foundBook.isPresent());
        assertEquals("Test Book", foundBook.get().getTitle());
    }

    @Test
    public void shouldReturnEmptyOptionalWhenTitleNotFound() {
        Optional<Book> foundBook = bookRepository.findByTitle("Non-Existent Book");
        assertFalse(foundBook.isPresent());
    }

    @Test
    public void shouldCheckIfBookExistsByTitle() {
        // Arrange
        Book book = new Book();
        book.setTitle("Existing Book");
        book.setAuthor("Author X");
        book.setGenre("History");
        book.setAvailabilityStatus("Available");
        book.setIsbn("9876543210");
        bookRepository.save(book);

        // Act & Assert
        assertTrue(bookRepository.existsByTitle("Existing Book"));
        assertFalse(bookRepository.existsByTitle("Random Book"));
    }

    @Test
    public void shouldFindBookByIsbn() {
        // Arrange
        Book book = new Book();
        book.setTitle("Java Programming");
        book.setAuthor("James Gosling");
        book.setGenre("Technology");
        book.setAvailabilityStatus("Available");
        book.setIsbn("1112223334");
        bookRepository.save(book);

        // Act
        Optional<Book> foundBook = bookRepository.findByIsbn("1112223334");

        // Assert
        assertTrue(foundBook.isPresent());
        assertEquals("Java Programming", foundBook.get().getTitle());
    }

    @Test
    public void shouldReturnEmptyOptionalWhenIsbnNotFound() {
        Optional<Book> foundBook = bookRepository.findByIsbn("0000000000");
        assertFalse(foundBook.isPresent());
    }
}
