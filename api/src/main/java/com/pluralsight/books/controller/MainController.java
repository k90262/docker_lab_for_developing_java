package com.pluralsight.books.controller;

import com.pluralsight.books.exception.BookNotFoundException;
import com.pluralsight.books.model.Book;
import com.pluralsight.books.model.Category;
import com.pluralsight.books.repository.BookRepository;
import com.pluralsight.books.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MainController {
    private Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/books")
    public List<Book> getBooks() {
        logger.debug("Entering \"/books\" path");
        return bookRepository.findAll();
    }

    @GetMapping("/books/categories")
    public List<Category> getCategories() {
        logger.debug("Entering \"/categories\" path");
        return categoryRepository.findAll();
    }

    @GetMapping("/books/categories/{categoryId}")
    public List<Book> getBooksFromCategory(@PathVariable Integer categoryId) {
        logger.debug("Entering \"/books/category/" + categoryId + " \" path");
        return bookRepository.findAllByCategoryId(categoryId);
    }

    @GetMapping("/books/{bookId}")
    public Book getBook(@PathVariable Long bookId) {
        logger.debug("Entering \"/books/" + bookId + " \" path");
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));
    }

}
