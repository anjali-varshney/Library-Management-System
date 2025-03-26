package com.ajackus.Library_Management_System.service.impl;

import com.ajackus.Library_Management_System.dto.BookDTO;
import com.ajackus.Library_Management_System.model.Book;
import com.ajackus.Library_Management_System.repository.BookRepository;
import com.ajackus.Library_Management_System.service.interfaces.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {

    private final ModelMapper modelMapper;
    private final BookRepository bookRepository;

    @Override
    public BookDTO addBook(BookDTO bookDto) {
        log.info("Adding a new book: {}", bookDto);
        Book book = modelMapper.map(bookDto, Book.class);
        book = bookRepository.save(book);
        log.info("Book added successfully with ID: {}", book.getId());
        return modelMapper.map(book, BookDTO.class);
    }

     // Retrieves all books from the library.
     @Override
    public List<BookDTO> getAllBooks() {
        log.info("Fetching all books");
        List<BookDTO> books = bookRepository.findAll().stream()
                .map(book -> modelMapper.map(book, BookDTO.class))
                .collect(Collectors.toList());
        log.info("Total books retrieved: {}", books.size());
        return books;
    }

    //Retrieves a book by its ID.
    @Override
    public BookDTO getBookById(Long id) {
        log.info("Fetching book with ID: {}", id);
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Book with ID {} not found", id);
                    return new RuntimeException("Book not found");
                });
        return modelMapper.map(book, BookDTO.class);
    }

    //Retrieves a book by its title.
    @Override
    public BookDTO getBookByTitle(String title) {
        log.info("Fetching book with title: {}", title);
        Optional<Book> book = bookRepository.findByTitle(title);
        return book.map(value -> modelMapper.map(value, BookDTO.class))
                .orElseThrow(() -> {
                    log.warn("Book with title '{}' not found", title);
                    return new RuntimeException("Book not found");
                });
    }

    //Updates an existing book's details.
    @Override
    public BookDTO updateBook(Long id, BookDTO bookDto) {
        log.info("Updating book with ID: {}", id);
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Book with ID {} not found", id);
                    return new RuntimeException("Book not found");
                });

        existingBook.setTitle(bookDto.getTitle());
        existingBook.setAuthor(bookDto.getAuthor());
        existingBook.setGenre(bookDto.getGenre());
        existingBook.setAvailabilityStatus(bookDto.getAvailabilityStatus());

        bookRepository.save(existingBook);
        log.info("Book with ID {} updated successfully", id);
        return modelMapper.map(existingBook, BookDTO.class);
    }

    //Deletes a book by its ID.
    @Override
    public void deleteBook(Long id) {
        log.info("Deleting book with ID: {}", id);
        if (!bookRepository.existsById(id)) {
            log.warn("Attempted to delete a non-existing book with ID: {}", id);
            throw new RuntimeException("Book not found");
        }
        bookRepository.deleteById(id);
        log.info("Book with ID {} deleted successfully", id);

    }
}


