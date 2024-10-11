package com.learning.RandomPosts.dto;

import jakarta.validation.constraints.NotBlank;

public record PostUpdateDto(
        @NotBlank(message = "Title is required")
        String title,
        @NotBlank(message = "Content for your post is required")
        String content) {
}
