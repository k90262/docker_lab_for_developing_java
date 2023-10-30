package com.pluralsight.books.controller;

import com.pluralsight.books.model.Book;
import com.pluralsight.books.repository.BookRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

@Controller
public class MainController {
    private Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private BookRepository bookRepository;

    @GetMapping("/")
    public String index(Model model) {
        logger.debug("Entering \"/\" path");

        // Get three book IDs
        Set<Long> ids = new HashSet<>();
        ids.add(6L);
        ids.add(10L);
        ids.add(15L);

        List<Book> books = bookRepository.findAllById(ids);
        model.addAttribute("books", books);

        return "index";
    }

    @GetMapping("/books/category/{categoryId}")
    public String getBooksFromCategory(@PageableDefault(size = 12) Pageable pageable,
                                       @PathVariable("categoryId") int categoryId,
                           Model model) {
        logger.debug("Entering \"/books/category/" + categoryId + " \" path");
        Page<Book> page = bookRepository.findAllByCategoryId(categoryId, pageable);
        model.addAttribute("page", page);
        return "books";
    }

    @GetMapping("/books/{bookId}")
    public String getBook(@PageableDefault(size = 10, sort = "registered", direction = Sort.Direction.DESC) Pageable pageable,
                          @PathVariable("bookId") long bookId,
                           Model model,
                          Principal principal) {
        logger.debug("Entering \"/books/" + bookId + " \" path");
        Book book = bookRepository.findById(bookId).get();

        model.addAttribute("book", book);

        return "book";
    }

    @GetMapping("/books/")
    public String getBooks(@PageableDefault(size = 12) Pageable pageable,
                           Model model) {
        logger.debug("Entering \"/books\" path");
        Page<Book> page = bookRepository.findAll(pageable);
        model.addAttribute("page", page);
        return "books";
    }

    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request) {
        return request.getRequestURI();
    }
}
