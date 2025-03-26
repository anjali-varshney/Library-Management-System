package com.ajackus.Library_Management_System.service.impl;

import com.ajackus.Library_Management_System.dto.BookDTO;
import com.ajackus.Library_Management_System.service.interfaces.BookService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class BookServiceImpl implements BookService {
    @Override
    public BookDTO addBook(BookDTO bookDto) {
        return null;
    }

    @Override
    public List<BookDTO> getAllBooks() {
        return List.of();
    }

    @Override
    public BookDTO getBookById(Long id) {
        return null;
    }

    @Override
    public BookDTO getBookByTitle(String title) {
        return null;
    }

    @Override
    public BookDTO updateBook(Long id, BookDTO bookDto) {
        return null;
    }

    @Override
    public void deleteBook(Long id) {

    }
}
