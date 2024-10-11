package com.learning.RandomPosts.dto;

import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record NewPostDto(
        @Min(value = 1, message = "Post type is required, accepted values are 1 - Informative, 2 - Announcement, 3 - Simple")
        @Max(value = 3, message = "Post type is required, accepted values are 1 - Informative, 2 - Announcement, 3 - Simple")
        @NotNull(message = "Post type must be provided. Accepted values are 1 - Informative, 2 - Announcement, 3 - Simple")
        Integer postType,
        @NotBlank(message = "Title is required")
        String title,
        @NotBlank(message = "Content for your post is required")
        String content,
        @NotBlank(message = "Author is required")
        String author,
        List<MultipartFile> attachments) {
}
