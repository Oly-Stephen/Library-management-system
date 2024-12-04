package com.oluwayanmi.library.service;

import com.oluwayanmi.library.exception.AuthorNotFoundException;
import com.oluwayanmi.library.exception.BookNotFoundException;
import com.oluwayanmi.library.model.Author;
import com.oluwayanmi.library.model.Book;
import com.oluwayanmi.library.repository.AuthorRepository;
import com.oluwayanmi.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookWithAuthor(Long id) {
        Book book = getBookById(id);
        book.getAuthor();
        return book;
    }

    public Book addBook(Book book, Long authorId) {
        if (authorId == null) {
            throw new IllegalArgumentException("Author ID must not be null");
        }
        Author author = authorRepository.findById(authorId).orElseThrow(() -> new AuthorNotFoundException(authorId));
        book.setAuthor(author);
        return bookRepository.save(book);
    }

    public void deleteBookById(Long id) {
        Book book = getBookById(id);
        bookRepository.delete(book);
    }
}