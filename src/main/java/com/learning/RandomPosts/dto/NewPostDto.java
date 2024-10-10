package com.learning.RandomPosts.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record NewPostDto(
        @Min(value = 1, message = "Post type is required, accepted values are 1 - Informative, 2 - Announcement, 3 - Simple")
        @Max(value = 3, message = "Post type is required, accepted values are 1 - Informative, 2 - Announcement, 3 - Simple")
        int postType,
        @NotBlank(message = "Title is required")
        String title,
        @NotBlank(message = "Content for your post is required")
        String content,
        @NotBlank(message = "Author is required")
        String author,
        List<MultipartFile> attachments) {

        public NewPostDto {
            if (attachments.size() > 1) {
                throw new IllegalArgumentException("Only one attachment is allowed");
            }
        }

}
