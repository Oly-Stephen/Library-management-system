package com.oluwayanmi.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oluwayanmi.library.model.Author;
import com.oluwayanmi.library.model.Book;
import com.oluwayanmi.library.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void getBookById_ShouldReturnBook() throws Exception {
        // Arrange
        Long bookId = 1L;
        Book book = new Book();
        book.setId(bookId);
        book.setTitle("Test Book");
        book.setDescription("Test Description");

        when(bookService.getBookById(bookId)).thenReturn(book);

        // Act & Assert
        mockMvc.perform(get("/api/books/{id}", bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookId))
                .andExpect(jsonPath("$.title").value("Test Book"))
                .andExpect(jsonPath("$.description").value("Test Description"));
    }

    @Test
    void getAllBooks_ShouldReturnListOfBooks() throws Exception {
        // Arrange
        List<Book> books = Arrays.asList(
            new Book(1L, "Book 1", "Description 1", null, null),
            new Book(2L, "Book 2", "Description 2", null, null)
        );

        when(bookService.getAllBooks()).thenReturn(books);

        // Act & Assert
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title").value("Book 1"))
                .andExpect(jsonPath("$[1].title").value("Book 2"));
    }

    @Test
    void getBookWithAuthors_ShouldReturnBookWithAuthor() throws Exception {
        // Arrange
        Long bookId = 1L;
        Author author = new Author(1L, "Test Author", "Bio", null, null);
        Book book = new Book(bookId, "Test Book", "Description", null, author);

        when(bookService.getBookWithAuthor(bookId)).thenReturn(book);

        // Act & Assert
        mockMvc.perform(get("/api/books/{id}/authors", bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Book"))
                .andExpect(jsonPath("$.author.name").value("Test Author"));
    }

    @Test
    void addBook_ShouldReturnSavedBook() throws Exception {
        // Arrange
        Long authorId = 1L;
        Book book = new Book(1L, "New Book", "New Description", null, null);
        MockMultipartFile bookImage = new MockMultipartFile(
            "bookImage",
            "test.jpg",
            MediaType.IMAGE_JPEG_VALUE,
            "test image content".getBytes()
        );

        when(bookService.addBook(any(Book.class), eq(authorId), any(MultipartFile.class)))
            .thenReturn(book);

        // Act & Assert
        mockMvc.perform(multipart("/api/books")
                .file(bookImage)
                .param("title", "New Book")
                .param("description", "New Description")
                .param("authorId", authorId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Book"))
                .andExpect(jsonPath("$.description").value("New Description"));
    }

    @Test
    void deleteBookById_ShouldReturnNoContent() throws Exception {
        // Arrange
        Long bookId = 1L;
        doNothing().when(bookService).deleteBookById(bookId);

        // Act & Assert
        mockMvc.perform(delete("/api/books/{id}", bookId))
                .andExpect(status().isNoContent());

        verify(bookService).deleteBookById(bookId);
    }
}