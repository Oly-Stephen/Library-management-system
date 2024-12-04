package com.oluwayanmi.library.controller.viewController;

import com.oluwayanmi.library.model.Author;
import com.oluwayanmi.library.repository.AuthorRepository;
import com.oluwayanmi.library.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class ThymeLeafAuthorController {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private AuthorService authorService;

    @GetMapping("/authors")
    public String getAuthors(Model model) {
        List<Author> authors = authorRepository.findAll();
        model.addAttribute("authors", authors);
        return "authors";
    }

    @GetMapping("/authors/new")
    public String newAuthorForm(Model model) {
        model.addAttribute("author", new Author());
        return "author-form";
    }

    @GetMapping("form/authors/{id}")
    public String editAuthorForm(@PathVariable Long id, Model model) {
        Optional<Author> author = authorRepository.findById(id);
        if (author.isPresent()) {
            model.addAttribute("author", author.get());
            return "author-form";
        }
        return "redirect:/authors";
    }

    @PostMapping("author/authors")
    public String addAuthor(@ModelAttribute Author author, Model model) {
        authorRepository.save(author);
        return "redirect:/authors";
    }

    @PutMapping("/authors/{id}")
    public String updateAuthor(@PathVariable Long id, @ModelAttribute Author author, Model model) {
        Optional<Author> existingAuthor = authorRepository.findById(id);
        if (existingAuthor.isPresent()) {
            Author updatedAuthor = existingAuthor.get();
            updatedAuthor.setName(author.getName());
            authorRepository.save(updatedAuthor);
        }
        return "redirect:/authors";
    }

    @DeleteMapping("/authors/{id}")
    public String deleteAuthor(@PathVariable Long id, Model model) {
        authorRepository.deleteById(id);
        return "redirect:/authors";
    }

    @GetMapping("/authors/{id}/books")
    public String getAuthorWithBooks(@PathVariable Long id, Model model) {
        Optional<Author> author = authorRepository.findById(id);
        if (author.isPresent()) {
            model.addAttribute("author", author.get());
            return "author-books";
        }
        return "redirect:/authors";
    }

    @GetMapping("/authors/{id}")
    public String getAuthor(@PathVariable Long id, Model model) {
        Author author = authorService.getAuthorById(id);
        model.addAttribute("author", author);
        return "author-detail";
    }
}