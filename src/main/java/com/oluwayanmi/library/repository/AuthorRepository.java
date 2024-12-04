package com.oluwayanmi.library.repository;

import com.oluwayanmi.library.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
