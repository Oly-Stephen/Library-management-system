package com.oluwayanmi.library.controller;

import com.oluwayanmi.library.model.Book;
import com.oluwayanmi.library.service.BookService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Book book = bookService.getBookById(id);
        return ResponseEntity.ok(book);
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}/authors")
    public ResponseEntity<Book> getBookWithAuthors(@PathVariable Long id) {
        Book book = bookService.getBookWithAuthor(id);
        return ResponseEntity.ok(book);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Book> addBook(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("authorId") Long authorId,
            @RequestParam(value = "bookImage", required = false) MultipartFile bookImage) {

        Book book = new Book();
        book.setTitle(title);
        book.setDescription(description);

        Book savedBook = bookService.addBook(book, authorId, bookImage);
        return ResponseEntity.ok(savedBook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookById(@PathVariable Long id) {
        bookService.deleteBookById(id);
        return ResponseEntity.noContent().build();
    }
}