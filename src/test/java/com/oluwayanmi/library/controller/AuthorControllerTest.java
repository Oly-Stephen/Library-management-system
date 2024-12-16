package com.oluwayanmi.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oluwayanmi.library.model.Author;
import com.oluwayanmi.library.model.Book;
import com.oluwayanmi.library.service.AuthorService;
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
class AuthorControllerTest {

    @Mock
    private AuthorService authorService;

    @InjectMocks
    private AuthorController authorController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authorController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void getAuthorById_ShouldReturnAuthor() throws Exception {
        // Arrange
        Long authorId = 1L;
        Author author = new Author(authorId, "Test Author", "Test Bio", null, null);

        when(authorService.getAuthorById(authorId)).thenReturn(author);

        // Act & Assert
        mockMvc.perform(get("/authors/{id}", authorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(authorId))
                .andExpect(jsonPath("$.name").value("Test Author"))
                .andExpect(jsonPath("$.bio").value("Test Bio"));
    }

    @Test
    void getAllAuthors_ShouldReturnListOfAuthors() throws Exception {
        // Arrange
        List<Author> authors = Arrays.asList(
            new Author(1L, "Author 1", "Bio 1", null, null),
            new Author(2L, "Author 2", "Bio 2", null, null)
        );

        when(authorService.getAllAuthors()).thenReturn(authors);

        // Act & Assert
        mockMvc.perform(get("/authors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Author 1"))
                .andExpect(jsonPath("$[1].name").value("Author 2"));
    }

    @Test
    void getAuthorWithBooks_ShouldReturnAuthorWithBooks() throws Exception {
        // Arrange
        Long authorId = 1L;
        List<Book> books = Arrays.asList(
            new Book(1L, "Book 1", "Description 1", null, null),
            new Book(2L, "Book 2", "Description 2", null, null)
        );
        Author author = new Author(authorId, "Test Author", "Bio", null, books);

        when(authorService.getAuthorWithBooks(authorId)).thenReturn(author);

        // Act & Assert
        mockMvc.perform(get("/authors/{id}/books", authorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Author"))
                .andExpect(jsonPath("$.books", hasSize(2)))
                .andExpect(jsonPath("$.books[0].title").value("Book 1"));
    }

    @Test
    void addAuthor_ShouldReturnSavedAuthor() throws Exception {
        // Arrange
        Author author = new Author(1L, "New Author", "New Bio", null, null);
        MockMultipartFile authorImage = new MockMultipartFile(
            "authorImage",
            "test.jpg",
            MediaType.IMAGE_JPEG_VALUE,
            "test image content".getBytes()
        );

        when(authorService.addAuthor(any(Author.class), any(MultipartFile.class)))
            .thenReturn(author);

        // Act & Assert
        mockMvc.perform(multipart("/authors")
                .file(authorImage)
                .param("name", "New Author")
                .param("bio", "New Bio"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Author"))
                .andExpect(jsonPath("$.bio").value("New Bio"));
    }

    @Test
    void updateAuthor_ShouldReturnUpdatedAuthor() throws Exception {
        // Arrange
        Long authorId = 1L;
        Author authorDetails = new Author(authorId, "Updated Author", "Updated Bio", null, null);
        
        when(authorService.updateAuthor(eq(authorId), any(Author.class)))
            .thenReturn(authorDetails);

        // Act & Assert
        mockMvc.perform(put("/authors/{id}", authorId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authorDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Author"))
                .andExpect(jsonPath("$.bio").value("Updated Bio"));
    }

    @Test
    void deleteAuthor_ShouldReturnNoContent() throws Exception {
        // Arrange
        Long authorId = 1L;
        doNothing().when(authorService).deleteAuthor(authorId);

        // Act & Assert
        mockMvc.perform(delete("/authors/{id}", authorId))
                .andExpect(status().isNoContent());

        verify(authorService).deleteAuthor(authorId);
    }
}