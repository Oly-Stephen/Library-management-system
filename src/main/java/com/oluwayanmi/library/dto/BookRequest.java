package com.oluwayanmi.library.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookRequest {
    private String title;
    private String description;
    private Long authorId;
}