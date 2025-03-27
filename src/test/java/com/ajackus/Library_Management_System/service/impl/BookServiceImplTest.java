package com.ajackus.Library_Management_System.service.impl;

import com.ajackus.Library_Management_System.dto.BookDTO;
import com.ajackus.Library_Management_System.exception.LibraryManagementException;
import com.ajackus.Library_Management_System.model.Book;
import com.ajackus.Library_Management_System.repository.BookRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book book;
    private BookDTO bookDto;

    @Before
    public void setUp() {
        book = new Book();
        book.setTitle("Test Book");
        book.setAuthor("John Doe");
        book.setGenre("Fiction");
        book.setAvailabilityStatus("Available");
        book.setIsbn("1234567890");

        bookDto = new BookDTO();
        bookDto.setTitle("Test Book");
        bookDto.setAuthor("John Doe");
        bookDto.setGenre("Fiction");
        bookDto.setAvailabilityStatus("Available");
        bookDto.setIsbn("1234567890");
    }

    @Test
    public void shouldAddBookSuccessfully() {
        when(bookRepository.findByIsbn(bookDto.getIsbn())).thenReturn(Optional.empty());
        when(bookRepository.existsByTitle(bookDto.getTitle())).thenReturn(false);
        when(modelMapper.map(bookDto, Book.class)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(modelMapper.map(book, BookDTO.class)).thenReturn(bookDto);

        BookDTO savedBook = bookService.addBook(bookDto);

        assertNotNull(savedBook);
        assertEquals("Test Book", savedBook.getTitle());
        verify(bookRepository, times(1)).save(book);
    }

    @Test(expected = LibraryManagementException.class)
    public void shouldThrowExceptionWhenIsbnAlreadyExists() {
        when(bookRepository.findByIsbn(bookDto.getIsbn())).thenReturn(Optional.of(book));
        bookService.addBook(bookDto);
    }

    @Test(expected = LibraryManagementException.class)
    public void shouldThrowExceptionWhenTitleAlreadyExists() {
        when(bookRepository.findByIsbn(bookDto.getIsbn())).thenReturn(Optional.empty());
        when(bookRepository.existsByTitle(bookDto.getTitle())).thenReturn(true);
        bookService.addBook(bookDto);
    }

    @Test
    public void shouldGetAllBooks() {
        when(bookRepository.findAll()).thenReturn(Arrays.asList(book));
        when(modelMapper.map(book, BookDTO.class)).thenReturn(bookDto);
        assertEquals(1, bookService.getAllBooks().size());
    }

    @Test
    public void shouldGetBookByIsbn() {
        when(bookRepository.findByIsbn("1234567890")).thenReturn(Optional.of(book));
        when(modelMapper.map(book, BookDTO.class)).thenReturn(bookDto);

        BookDTO foundBook = bookService.getBookByISBN("1234567890");

        assertNotNull(foundBook);
        assertEquals("Test Book", foundBook.getTitle());
    }

    @Test(expected = LibraryManagementException.class)
    public void shouldThrowExceptionWhenIsbnNotFound() {
        when(bookRepository.findByIsbn("0000000000")).thenReturn(Optional.empty());
        bookService.getBookByISBN("0000000000");
    }

    @Test
    public void shouldGetBookByTitle() {
        when(bookRepository.findByTitle("Test Book")).thenReturn(Optional.of(book));
        when(modelMapper.map(book, BookDTO.class)).thenReturn(bookDto);
        BookDTO foundBook = bookService.getBookByTitle("Test Book");
        assertNotNull(foundBook);
        assertEquals("Test Book", foundBook.getTitle());
    }

    @Test(expected = LibraryManagementException.class)
    public void shouldThrowExceptionWhenBookTitleNotFound() {
        when(bookRepository.findByTitle("NonExisting")).thenReturn(Optional.empty());
        bookService.getBookByTitle("NonExisting");
    }



    @Test(expected = LibraryManagementException.class)
    public void shouldThrowExceptionWhenUpdatingNonExistingBook() {
        when(bookRepository.findByIsbn("0000000000")).thenReturn(Optional.empty());
        bookService.updateBook("0000000000", bookDto);
    }

    @Test
    public void shouldDeleteBookSuccessfully() {
        when(bookRepository.findByIsbn("1234567890")).thenReturn(Optional.of(book));
        doNothing().when(bookRepository).delete(book);
        bookService.deleteBook("1234567890");
        verify(bookRepository, times(1)).delete(book);
    }

    @Test(expected = LibraryManagementException.class)
    public void shouldThrowExceptionWhenDeletingNonExistingBook() {
        when(bookRepository.findByIsbn("0000000000")).thenReturn(Optional.empty());
        bookService.deleteBook("0000000000");
    }
}
