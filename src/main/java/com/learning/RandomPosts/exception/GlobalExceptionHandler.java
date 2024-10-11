package com.learning.RandomPosts.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final int SQL_STATE_UNIQUE_CONSTRAINT_VIOLATION = 23505;
    private static final int ATTACHMENT_ARGUMENT_SIZE_EXCEEDED = 21111;
    private static final int NULL_POINTER_EXCEPTION = 33333;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleMethodArgumentExceptions(MethodArgumentNotValidException exception) {
        List<String> errorsList = exception.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
        ErrorMessage errorMessage = new ErrorMessage(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),0 , errorsList);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorMessage> handleDataIntegrityViolationException(DataIntegrityViolationException exception, HttpServletRequest request) {
        List<String> errorsList = List.of("Post already exists");
        ErrorMessage errorMessage = new ErrorMessage(LocalDateTime.now(), HttpStatus.CONFLICT.value(), SQL_STATE_UNIQUE_CONSTRAINT_VIOLATION, errorsList);
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .location(URI.create(
                        String.format("/posts/%s", request.getParameter("title")
                                .replaceAll(" ", "-")
                                .toLowerCase()
                        )
                ))
                .body(errorMessage);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorMessage> handleIllegalArgumentException(IllegalArgumentException exception) {
        List<String> errorsList = List.of("Attachment quantity is limited to one.");
        ErrorMessage errorMessage = new ErrorMessage(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), ATTACHMENT_ARGUMENT_SIZE_EXCEEDED, errorsList);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorMessage> handleException(Exception exception) {
        ErrorMessage errorMessage = new ErrorMessage(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), NULL_POINTER_EXCEPTION, List.of("Internal Server Error. Please report to an administrator"));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
    }
}
