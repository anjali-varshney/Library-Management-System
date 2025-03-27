package com.ajackus.Library_Management_System.controller;

import com.ajackus.Library_Management_System.dto.BookDTO;
import com.ajackus.Library_Management_System.exception.LibraryManagementException;
import com.ajackus.Library_Management_System.service.interfaces.BookService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.ui.Model;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class WebControllerTest {

    @Mock
    private BookService bookService;

    @Mock
    private Model model;

    @InjectMocks
    private WebController webController;

    @Test
    public void shouldReturnAddBookFormView() {
        String viewName = webController.showAddBookForm(model);

        assertEquals("addbooks", viewName);
        verify(model).addAttribute(eq("bookDTO"), any(BookDTO.class));
        verifyNoMoreInteractions(model);
    }

    @Test
    public void shouldAddBookSuccessfully() {
        BookDTO bookDTO = new BookDTO();
        when(bookService.addBook(bookDTO)).thenReturn(bookDTO); // Correct way

        String viewName = webController.addBook(bookDTO, model);

        assertEquals("addbooks", viewName);
        verify(bookService).addBook(bookDTO);
        verify(model).addAttribute("successMessage", "Book added successfully!");
        verifyNoMoreInteractions(model);
    }

    @Test
    public void shouldHandleAddBookFailure() {
        BookDTO bookDTO = new BookDTO();
        doThrow(new LibraryManagementException("Error adding book"))
                .when(bookService).addBook(bookDTO);

        String viewName = webController.addBook(bookDTO, model);

        assertEquals("addbooks", viewName);
        verify(model).addAttribute("errorMessage", "Error adding book");
        verifyNoMoreInteractions(model);
    }

    @Test
    public void shouldReturnViewAllBooksPage() {
        List<BookDTO> bookList = Collections.emptyList();
        when(bookService.getAllBooks()).thenReturn(bookList);

        String viewName = webController.viewAllBooks(model);

        assertEquals("viewbooks", viewName);
        verify(model).addAttribute("books", bookList);
        verifyNoMoreInteractions(model);
    }

    @Test
    public void shouldReturnSearchBookPageWhenBookFound() {
        String title = "Book Title";
        BookDTO bookDTO = new BookDTO();
        when(bookService.getBookByTitle(title)).thenReturn(bookDTO);

        String viewName = webController.searchBook(title, false, model);

        assertEquals("searchbooks", viewName);
        verify(model).addAttribute("book", bookDTO);
        verify(model).addAttribute("updateMode", false);
        verifyNoMoreInteractions(model);
    }

    @Test
    public void shouldReturnSearchBookPageWhenBookNotFound() {
        String title = "Non-Existent Title";
        when(bookService.getBookByTitle(title)).thenReturn(null);

        String viewName = webController.searchBook(title, false, model);

        assertEquals("searchbooks", viewName);
        verify(model).addAttribute("message", "No book found with the given title.");
        verifyNoMoreInteractions(model);
    }
}
