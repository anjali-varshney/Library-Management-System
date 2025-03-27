package com.ajackus.Library_Management_System.controller;

import com.ajackus.Library_Management_System.dto.BookDTO;
import com.ajackus.Library_Management_System.model.Book;
import com.ajackus.Library_Management_System.repository.BookRepository;
import com.ajackus.Library_Management_System.service.interfaces.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/books")
@Slf4j
@RequiredArgsConstructor
public class HomeController {

    private final BookService bookService;
    private final BookRepository bookRepository;

    @GetMapping
    public String home() {
        return "index";
    }

    @GetMapping("/add")
    public String showAddBookForm(Model model) {
        model.addAttribute("bookDTO", new BookDTO());
        return "addbooks";
    }

    @PostMapping("/add")
    public String addBook(@ModelAttribute("bookDTO") BookDTO bookDTO, RedirectAttributes redirectAttributes) {
        log.info("Received request to add book: {}", bookDTO);
        bookDTO.setAvailabilityStatus("Available");
        bookService.addBook(bookDTO);
        log.info("Book added successfully!");
        redirectAttributes.addFlashAttribute("successMessage", "Book added successfully!");
        return "redirect:/books/add";
    }

    @GetMapping("/view")
    public String viewAllBooks(Model model) {
        log.info("Fetching all books from the database...");
        List<BookDTO> books = bookService.getAllBooks();
        log.info("Total books fetched: {}", books.size());
        model.addAttribute("books", books);

        return "viewbooks"; // Return the Thymeleaf page
    }

    @GetMapping("/search")
    public String searchBook(@RequestParam(name = "title", required = false) String title, Model model) {
        if (title == null || title.trim().isEmpty()) {
            log.info("No title provided, returning empty search page.");
            model.addAttribute("book", null);
            return "searchbooks";
        }

        log.info("Searching for book with title: {}", title);
        BookDTO book = bookService.getBookByTitle(title);

        if (book == null) {
            log.info("Book not found with title: {}", title);
            model.addAttribute("message", "No book found with the given title.");
        }

        model.addAttribute("book", book);
        return "searchbooks";
    }

    @GetMapping("/update")
    public String showUpdateBookForm(@RequestParam(name = "title", required = false) String title,
                                     @RequestParam(name = "id", required = false) Long id,
                                     Model model) {
        BookDTO book = null;

        if (id != null) {
            log.info("Searching for book with ISBN: '{}'", id);
            book = bookService.getBookById(id);
        } else if (title != null && !title.trim().isEmpty()) {
            log.info("Searching for book with Title: '{}'", title);
            book = bookService.getBookByTitle(title);
        }

        if (book == null) {
            log.info("Book not found!");
            model.addAttribute("message", "No book found with the given details.");
            return "updatebooks";
        }

        model.addAttribute("book", book);
        return "updatebooks";
    }


    @PostMapping("/update")
    public BookDTO updateBook(Long id, BookDTO bookDto) {
        Book existingBook = bookRepository.findById(id).orElse(null);

        if (existingBook != null) {
            existingBook.setTitle(bookDto.getTitle());
            existingBook.setAuthor(bookDto.getAuthor());
            existingBook.setGenre(bookDto.getGenre());
            existingBook.setAvailabilityStatus(bookDto.getAvailabilityStatus());

            bookRepository.save(existingBook);
            log.info("Book with ID {} updated successfully.", id);
            return new BookDTO(existingBook);
        } else {
            log.warn("Book with ID {} not found! Update failed.", id);
            return null;
        }
    }


}


