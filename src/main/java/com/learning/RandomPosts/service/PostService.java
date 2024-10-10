package com.learning.RandomPosts.service;

import com.learning.RandomPosts.dto.NewPostDto;
import com.learning.RandomPosts.model.AbstractPost;
import com.learning.RandomPosts.model.PostFactory;
import com.learning.RandomPosts.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PostService {

    PostRepository postRepository;

    public PostService(PostRepository postRepository){
        this.postRepository = postRepository;
    }

    public Optional<AbstractPost> createPost(NewPostDto newPostDto){
        Optional<AbstractPost> newPost = Optional.of(PostFactory.createPostByType(newPostDto));
        postRepository.save(newPost.get());
        return newPost;
    }
}
