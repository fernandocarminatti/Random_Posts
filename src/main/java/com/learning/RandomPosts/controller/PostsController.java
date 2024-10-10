package com.learning.RandomPosts.controller;

import com.learning.RandomPosts.dto.NewPostDto;
import com.learning.RandomPosts.model.AbstractPost;
import com.learning.RandomPosts.service.PostService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;
import java.util.Optional;

@Controller
@RequestMapping("/posts")
public class PostsController {
    PostService postService;

    public PostsController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<String> getPosts() {
        return ResponseEntity.ok("Hello World");
    }

    @PostMapping("/new-post")
    public ResponseEntity<?> createPost(@Valid @ModelAttribute NewPostDto newPostDto) {
        Optional<AbstractPost> post = postService.createPost(newPostDto);
        return post.isEmpty() ? ResponseEntity.badRequest().body("Post creation failed") : ResponseEntity.created(URI.create(String.format("/posts/%s", post.get().getId()))).body("Post created successfully");
    }
}
