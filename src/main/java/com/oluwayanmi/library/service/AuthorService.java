package com.oluwayanmi.library.service;

import com.oluwayanmi.library.exception.AuthorNotFoundException;
import com.oluwayanmi.library.model.Author;
import com.oluwayanmi.library.repository.AuthorRepository;
import com.oluwayanmi.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    public Author getAuthorById(Long id) {
        return authorRepository.findById(id).orElseThrow(() -> new AuthorNotFoundException(id));
    }

    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    public Author getAuthorWithBooks(Long id) {
        Author author = getAuthorById(id);
        author.getBooks().size();
        return author;
    }

    public Author addAuthor(Author author) {
        return authorRepository.save(author);
    }

    public void deleteAuthor(Long id) {
        Author author = getAuthorById(id);
        bookRepository.deleteAll(author.getBooks());
        authorRepository.delete(author);
    }

        public Author updateAuthor(Long id, Author authorDetails) {
        Author author = authorRepository.findById(id).orElseThrow(() -> new AuthorNotFoundException(id));
        author.setName(authorDetails.getName());
        return authorRepository.save(author);
    }
}