package com.oluwayanmi.library.service;

import com.oluwayanmi.library.model.Author;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AuthorService {
    Author getAuthorById(Long id);
    List<Author> getAllAuthors();
    Author getAuthorWithBooks(Long id);
    Author addAuthor(Author author, MultipartFile authorImage);
    Author updateAuthor(Long id, Author authorDetails);
    void deleteAuthor(Long id);
}