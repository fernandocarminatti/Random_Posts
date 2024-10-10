package com.learning.RandomPosts.dto;

import com.learning.RandomPosts.model.AbstractPost;

import java.time.LocalDate;

public record PostResponseDto(String id, String title, String content, String author, String attachments, LocalDate createdAt, LocalDate updatedAt){

    public static PostResponseDto fromEntity(AbstractPost abstractPost){
        return new PostResponseDto(
                abstractPost.getId(),
                abstractPost.getTitle(),
                abstractPost.getContent(),
                abstractPost.getAuthor(),
                abstractPost.getAttachments(),
                abstractPost.getCreatedAt(),
                abstractPost.getUpdatedAt()
        );
    }

}
