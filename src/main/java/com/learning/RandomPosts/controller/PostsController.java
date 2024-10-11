package com.learning.RandomPosts.controller;

import com.learning.RandomPosts.dto.NewPostDto;
import com.learning.RandomPosts.dto.PostResponseDto;
import com.learning.RandomPosts.dto.PostUpdateDto;
import com.learning.RandomPosts.model.AbstractPost;
import com.learning.RandomPosts.service.PostService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/posts")
public class PostsController {
    PostService postService;

    public PostsController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getPosts() {
        List<PostResponseDto> posts = postService.getAllPosts().stream().map(PostResponseDto::fromEntity).toList();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{title}")
    public ResponseEntity<PostResponseDto> getPostByTitle(@PathVariable String title) {
        String sanitizedTitle = title.replaceAll("-", " ");
        Optional<AbstractPost> post = postService.getPostByTitle(sanitizedTitle);
        return post.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(PostResponseDto.fromEntity(post.get()));
    }

    @PostMapping("/new-post")
    public ResponseEntity<?> createPost(@Valid @ModelAttribute NewPostDto newPostDto) {
        Optional<AbstractPost> post = postService.createPost(newPostDto);
        if (post.isEmpty()) {
            return ResponseEntity.internalServerError().body("Something went wrong. Please try again later");
        }
        URI location = URI.create(String.format("/posts/%s", post.get().getTitle().replaceAll(" ", "-").toLowerCase()));
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{title}")
    public ResponseEntity<?> updatePost(@Valid @RequestBody PostUpdateDto postUpdateDto, @PathVariable String title) {
        String sanitizedTitle = title.replaceAll("-", " ");
        Optional<AbstractPost> updatedPost = postService.updatePostData(postUpdateDto, sanitizedTitle);
        if(updatedPost.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedPost.get());
    }

    @DeleteMapping("/{title}")
    public ResponseEntity<?> deletePost(@PathVariable String title) {
        String sanitizedTitle = title.replaceAll("-", " ");
        boolean deletedPost = postService.deletePost(sanitizedTitle);
        return deletedPost ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
