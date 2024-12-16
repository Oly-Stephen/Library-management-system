package com.oluwayanmi.library.service;

import com.oluwayanmi.library.model.Book;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BookService {
    Book getBookById(Long id);
    List<Book> getAllBooks();
    Book getBookWithAuthor(Long id);
    Book addBook(Book book, Long authorId, MultipartFile bookImage);
    void deleteBookById(Long id);
}