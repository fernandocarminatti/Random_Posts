package com.learning.RandomPosts.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorMessage(LocalDateTime timestamp, HttpStatus status, List<String> errors) {
}
