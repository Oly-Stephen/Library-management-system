package com.oluwayanmi.library.controller;

import com.oluwayanmi.library.service.BookService;
import com.oluwayanmi.library.dto.BookRequest;
import com.oluwayanmi.library.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/{id}")
    public Book getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}/author")
    public Book getBookWithAuthor(@PathVariable Long id) {
        return bookService.getBookWithAuthor(id);
    }

    @PostMapping
    public Book addBook(@RequestBody BookRequest bookRequest) {
        Book book = new Book();
        book.setTitle(bookRequest.getTitle());
        book.setDescription(bookRequest.getDescription());
        return bookService.addBook(book, bookRequest.getAuthorId());
    }

    @DeleteMapping("/{id}")
    public void deleteBookById(@PathVariable Long id) {
        bookService.deleteBookById(id);
    }
}