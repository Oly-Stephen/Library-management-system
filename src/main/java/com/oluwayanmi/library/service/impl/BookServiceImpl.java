package com.oluwayanmi.library.service.impl;

import com.oluwayanmi.library.model.Author;
import com.oluwayanmi.library.model.Book;
import com.oluwayanmi.library.repository.AuthorRepository;
import com.oluwayanmi.library.repository.BookRepository;
import com.oluwayanmi.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Override
    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book getBookWithAuthor(Long id) {
        return bookRepository.findById(id).map(book -> {
            book.getAuthor().getName(); // Load author details
            return book;
        }).orElse(null);
    }

    @Override
    public Book addBook(Book book, Long authorId, MultipartFile bookImage) {
        Author author = authorRepository.findById(authorId).orElseThrow(() -> new RuntimeException("Author not found"));
        book.setAuthor(author);
        if (bookImage != null && !bookImage.isEmpty()) {
            try {
                book.setBookImage(bookImage.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Failed to store book image", e);
            }
        }
        return bookRepository.save(book);
    }

    @Override
    public void deleteBookById(Long id) {
        bookRepository.deleteById(id);
    }
}