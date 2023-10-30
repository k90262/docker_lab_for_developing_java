package com.pluralsight.books.repository;

import com.pluralsight.books.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findAllByCategoryId(Integer id);
}
