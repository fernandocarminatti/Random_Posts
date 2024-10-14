package com.learning.RandomPosts.model;

import com.learning.RandomPosts.dto.NewPostDto;

public class PostFactory {

    public static AbstractPost createPostByType(NewPostDto newPostDto, String attachmentPath){
        return switch (newPostDto.postType()) {
            case 1 ->
                    new InformativePost(newPostDto.title(), newPostDto.content(), newPostDto.author(), attachmentPath);
            case 2 ->
                    new AnnouncementPost(newPostDto.title(), newPostDto.content(), newPostDto.author(), attachmentPath);
            case 3 ->
                    new SimplePost(newPostDto.title(), newPostDto.content(), newPostDto.author(), attachmentPath);
            default -> throw new IllegalArgumentException("Invalid post type");
        };
    }
}
