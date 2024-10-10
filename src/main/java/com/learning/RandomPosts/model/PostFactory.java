package com.learning.RandomPosts.model;

import com.learning.RandomPosts.dto.NewPostDto;

public class PostFactory {

    public static AbstractPost createPostByType(NewPostDto newPostDto){
        return switch (newPostDto.postType()) {
            case 1 ->
                    new InformativePost(newPostDto.title(), newPostDto.content(), newPostDto.author(), "Informative Post with attachment");
            case 2 ->
                    new AnnouncementPost(newPostDto.title(), newPostDto.content(), newPostDto.author(), "Announcement Post with attachment");
            case 3 ->
                    new SimplePost(newPostDto.title(), newPostDto.content(), newPostDto.author(), "Simple Post with attachment");
            default -> throw new IllegalArgumentException("Invalid post type");
        };
    }
}
