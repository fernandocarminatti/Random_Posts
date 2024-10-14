package com.learning.RandomPosts.exception;

public class InternalStorageException extends RuntimeException{
    public InternalStorageException(String message){
        super(message);
    }

    public InternalStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
