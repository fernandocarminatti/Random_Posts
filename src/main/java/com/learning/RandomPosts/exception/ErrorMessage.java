package com.learning.RandomPosts.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorMessage(LocalDateTime timestamp, int status, int errorCode, List<String> errorsList) {
}
