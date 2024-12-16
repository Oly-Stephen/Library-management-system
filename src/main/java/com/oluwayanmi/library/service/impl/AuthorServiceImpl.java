package com.oluwayanmi.library.service.impl;

import com.oluwayanmi.library.model.Author;
import com.oluwayanmi.library.repository.AuthorRepository;
import com.oluwayanmi.library.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Override
    public Author getAuthorById(Long id) {
        return authorRepository.findById(id).orElse(null);
    }

    @Override
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    @Override
    public Author getAuthorWithBooks(Long id) {
        return authorRepository.findById(id).map(author -> {
            author.getBooks().size(); // Load books details
            return author;
        }).orElse(null);
    }

    @Override
    public Author addAuthor(Author author, MultipartFile authorImage) {
        if (authorImage != null && !authorImage.isEmpty()) {
            try {
                author.setAuthorImage(authorImage.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Failed to store author image", e);
            }
        }
        return authorRepository.save(author);
    }

    @Override
    public Author updateAuthor(Long id, Author authorDetails) {
        Author author = authorRepository.findById(id).orElseThrow(() -> new RuntimeException("Author not found"));
        author.setName(authorDetails.getName());
        author.setBio(authorDetails.getBio());
        return authorRepository.save(author);
    }

    @Override
    public void deleteAuthor(Long id) {
        authorRepository.deleteById(id);
    }
}