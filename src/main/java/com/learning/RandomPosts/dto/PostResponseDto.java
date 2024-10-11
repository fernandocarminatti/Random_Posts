package com.learning.RandomPosts.dto;

import com.learning.RandomPosts.model.AbstractPost;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PostResponseDto(String title, String content, String author, String attachments, LocalDateTime createdAt, LocalDateTime updatedAt){

    public static PostResponseDto fromEntity(AbstractPost abstractPost){
        return new PostResponseDto(
                abstractPost.getTitle(),
                abstractPost.getContent(),
                abstractPost.getAuthor(),
                abstractPost.getAttachments(),
                abstractPost.getCreatedAt(),
                abstractPost.getUpdatedAt()
        );
    }

}
