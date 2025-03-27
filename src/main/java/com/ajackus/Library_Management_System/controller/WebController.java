package com.ajackus.Library_Management_System.controller;

import com.ajackus.Library_Management_System.dto.BookDTO;
import com.ajackus.Library_Management_System.exception.LibraryManagementException;
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
import java.util.Optional;

@Controller
@RequestMapping("/books")
@Slf4j
@RequiredArgsConstructor
public class WebController {

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
        public String addBook(@ModelAttribute("bookDTO") BookDTO bookDTO, Model model) {
            try {
                log.info("Received request to add book: {}", bookDTO);
                bookDTO.setAvailabilityStatus("Available");
                bookService.addBook(bookDTO);
                log.info("Book added successfully!");
                model.addAttribute("successMessage", "Book added successfully!");
            } catch (LibraryManagementException e) {
                log.error("Error adding book: {}", e.getMessage());
                model.addAttribute("errorMessage", e.getMessage()); // Add error message to model
            }
            return "addbooks";  // Return to the same page (don't use redirect)
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
        public String searchBook(@RequestParam(name = "title", required = false) String title,
                                 @RequestParam(name = "updateMode", required = false, defaultValue = "false") boolean updateMode,
                                 Model model) {
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
                return "searchbooks";
            }

            model.addAttribute("book", book);
            model.addAttribute("updateMode", updateMode);

            return "searchbooks"; // Same page handles both cases
        }
    // Step 1: Show the search page
        @GetMapping("/update")
        public String showUpdateSearchForm(Model model) {
            model.addAttribute("book", null);
            model.addAttribute("updateMode", true);  // Indicate update mode
            return "searchbooks";  // Use merged page
        }

        // Step 2: Handle search request
        @GetMapping("/books/search")
        public String searchBook(@RequestParam(name = "title", required = false) String title,
                                 @RequestParam(name = "isbn", required = false) String isbn,
                                 Model model) {
            BookDTO book = null;

            if (isbn != null && !isbn.trim().isEmpty()) {
                book = bookService.getBookByISBN(isbn);
            } else if (title != null && !title.trim().isEmpty()) {
                book = bookService.getBookByTitle(title);
            }

            model.addAttribute("book", book);
            model.addAttribute("updateMode", book != null); // Ensure update button appears only if book is found

            if (book == null) {
                model.addAttribute("message", "Book not available.");
            }

            return "searchbooks";  // Stay on the same page with results
        }

        // Step 3: Redirect to update form
        @GetMapping("/update/form")
        public String showUpdateForm(@RequestParam("isbn") String isbn, Model model) {
            BookDTO book = bookService.getBookByISBN(isbn);
            if (book == null) {
                model.addAttribute("message", "Book not found.");
                return "searchbooks";
            }
            model.addAttribute("book", book);
            return "updatebooks"; // Redirect to update form
        }

        // Step 4: Handle book update
        @PostMapping("/update")
        public String updateBook(@ModelAttribute("book") BookDTO bookDto, RedirectAttributes redirectAttributes) {
            Book existingBook = bookRepository.findByIsbn(bookDto.getIsbn())
                    .orElse(null);

            if (existingBook != null) {
                existingBook.setTitle(bookDto.getTitle());
                existingBook.setAuthor(bookDto.getAuthor());
                existingBook.setGenre(bookDto.getGenre());
                existingBook.setAvailabilityStatus(bookDto.getAvailabilityStatus());

                bookRepository.save(existingBook);
                redirectAttributes.addFlashAttribute("successMessage", "Book updated successfully!");
                return "redirect:/books";
            } else {
                redirectAttributes.addFlashAttribute("message", "Book update failed. Book not found.");
                return "redirect:/books";
            }
        }

        //To delete a book
        @GetMapping("/delete")
        public String showDeletePage() {
            return "deletebook";
        }

        @PostMapping("/delete")
        public String deleteBook(@RequestParam("isbn") String isbn, RedirectAttributes redirectAttributes) {
            Optional<Book> book = bookRepository.findByIsbn(isbn);

            if (book.isPresent()) {
                bookRepository.delete(book.get());
                redirectAttributes.addFlashAttribute("successMessage", "Book deleted successfully!");
                return "redirect:/books";
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Book not found!");
            }

            return "redirect:/books/delete"; // Redirect back to delete form
        }


}


