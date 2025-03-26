package com.ajackus.Library_Management_System.repository;

import com.ajackus.Library_Management_System.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    /**
     * Finds a book by its title.
     * @param title The title of the book.
     * @return An optional Book entity.
     */

    Optional<Book> findByTitle(String title);
}
