package com.ajackus.Library_Management_System.service.impl;

import com.ajackus.Library_Management_System.dto.BookDTO;
import com.ajackus.Library_Management_System.exception.LibraryManagementException;
import com.ajackus.Library_Management_System.model.Book;
import com.ajackus.Library_Management_System.repository.BookRepository;
import com.ajackus.Library_Management_System.service.interfaces.BookService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {

    private final ModelMapper modelMapper;
    private final BookRepository bookRepository;

    @Override
    public BookDTO addBook(BookDTO bookDto) {
        log.info("Adding new book: {}", bookDto.getTitle());

        // Check if a book with the same ISBN already exists
        if (bookRepository.findByIsbn(bookDto.getIsbn()).isPresent()) {
            throw new LibraryManagementException("A book with ISBN " + bookDto.getIsbn() + " already exists. Please use a different ISBN.");
        }

        // Check if a book with the same Title already exists
        if (bookRepository.existsByTitle(bookDto.getTitle())) {
            throw new LibraryManagementException("A book with the title '" + bookDto.getTitle() + "' already exists.");
        }

        // Map DTO to entity and save
        Book book = modelMapper.map(bookDto, Book.class);
        bookRepository.save(book);

        return modelMapper.map(book, BookDTO.class);
    }


    @Override
    public List<BookDTO> getAllBooks() {
        log.info("Fetching all books");
        List<BookDTO> books = bookRepository.findAll().stream()
                .map(book -> modelMapper.map(book, BookDTO.class))
                .collect(Collectors.toList());

        log.info("Total books retrieved: {}", books.size());
        return books;
    }

    @Override
    public BookDTO getBookByISBN(String isbn) {
        log.info("Fetching book with ISBN: {}", isbn);
        Book book = (Book) bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new LibraryManagementException("Book not found with ISBN: " + isbn));

        return modelMapper.map(book, BookDTO.class);
    }


    @Override
    public BookDTO getBookByTitle(String title) {
        log.info("Fetching book with title: {}", title);
        Book book = bookRepository.findByTitle(title)
                .orElseThrow(() -> {
                    log.warn("Book with title '{}' not found", title);
                    return new LibraryManagementException("Book not found with title: " + title);
                });

        return modelMapper.map(book, BookDTO.class);
    }

    @Override
    @Transactional
    public BookDTO updateBook(String isbn, BookDTO bookDto) {
        log.info("Updating book with ISBN: {}", isbn);

        Book existingBook = (Book) bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> {
                    log.warn("Book with ISBN {} not found", isbn);
                    return new LibraryManagementException("Book not found with ISBN: " + isbn);
                });

        existingBook.setTitle(bookDto.getTitle());
        existingBook.setAuthor(bookDto.getAuthor());
        existingBook.setGenre(bookDto.getGenre());
        existingBook.setAvailabilityStatus(bookDto.getAvailabilityStatus());

        bookRepository.save(existingBook);
        log.info("Book with ISBN {} updated successfully", isbn);

        return modelMapper.map(existingBook, BookDTO.class);
    }

    @Override
    public void deleteBook(String isbn) {
        log.info("Deleting book with ISBN: {}", isbn);

        Book book = (Book) bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> {
                    log.warn("Attempted to delete a non-existing book with ISBN: {}", isbn);
                    return new LibraryManagementException("Book with ISBN " + isbn + " not found");
                });
        bookRepository.delete(book);
        log.info("Book with ISBN {} deleted successfully", isbn);
    }

}
