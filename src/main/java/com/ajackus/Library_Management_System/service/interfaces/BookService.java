package com.ajackus.Library_Management_System.service.interfaces;
import com.ajackus.Library_Management_System.dto.BookDTO;
import java.util.List;

public interface BookService {

    public BookDTO addBook(BookDTO bookDto);
    public List<BookDTO> getAllBooks();
    public BookDTO getBookByISBN(String isbn);
    public BookDTO getBookByTitle(String title);
    public BookDTO updateBook(String isbn, BookDTO bookDto);
    public void deleteBook(String isbn);
}
