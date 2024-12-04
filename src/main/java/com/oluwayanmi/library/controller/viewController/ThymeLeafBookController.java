package com.oluwayanmi.library.controller.viewController;

import com.oluwayanmi.library.model.Book;
import com.oluwayanmi.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ThymeLeafBookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/books")
    public String getAllBooks(Model model) {
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        return "books";
    }

    @GetMapping("/books/{id}")
    public String getBook(@PathVariable Long id, Model model) {
        Book book = bookService.getBookById(id);
        model.addAttribute("book", book);
        return "book-detail";
    }

    @GetMapping("/books/author/{id}")
    public String getBookWithAuthor(@PathVariable Long id, Model model) {
        Book book = bookService.getBookWithAuthor(id);
        model.addAttribute("book", book);
        return "author-detail";
    }

    @GetMapping("/books/new")
    public String newBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "book-form";
    }

    @PostMapping("/books")
    public String addBook(@ModelAttribute Book book, @RequestParam Long authorId) {
        book.setDescription(book.getDescription()); // Correct field name
        bookService.addBook(book, authorId);
        return "redirect:/books";
    }

    @DeleteMapping("/books/{id}")
    public String deleteBookById(@PathVariable Long id) {
        bookService.deleteBookById(id);
        return "redirect:/books";
    }
}