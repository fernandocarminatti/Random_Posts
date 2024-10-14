package com.learning.RandomPosts.service;

import com.learning.RandomPosts.dto.NewPostDto;
import com.learning.RandomPosts.dto.PostUpdateDto;
import com.learning.RandomPosts.model.AbstractPost;
import com.learning.RandomPosts.model.PostFactory;
import com.learning.RandomPosts.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PostService {

    PostRepository postRepository;
    StorageService storageService;

    public PostService(PostRepository postRepository, StorageService storageService) {
        this.postRepository = postRepository;
        this.storageService = storageService;
    }

    public List<AbstractPost> getAllPosts(){
        return postRepository.findAll();
    }

    public Optional<AbstractPost> getPostByTitle(String title){
        return postRepository.findByTitleIgnoreCase(title);
    }

    public Optional<AbstractPost> createPost(NewPostDto newPostDto){
        String folderId = UUID.randomUUID().toString();
        storageService.store(newPostDto.attachments(), folderId);
        Optional<AbstractPost> newPost = Optional.of(PostFactory.createPostByType(newPostDto, folderId));
        postRepository.save(newPost.get());
        return newPost;
    }

    public boolean deletePost(String title) {
        Optional<AbstractPost> postExistence = postRepository.findByTitleIgnoreCase(title);
        if(postExistence.isEmpty()){
            return false;
        }
        postRepository.delete(postExistence.get());
        return true;
    }

    public Optional<AbstractPost> updatePostData(PostUpdateDto postUpdateDto, String originalTitle) {
        Optional<AbstractPost> foundPost = postRepository.findByTitleIgnoreCase(originalTitle);
        if(foundPost.isEmpty()){
            return Optional.empty();
        }
        foundPost.get().setTitle(postUpdateDto.title());
        foundPost.get().setContent(postUpdateDto.content());
        foundPost.get().setUpdatedAt(LocalDateTime.now());
        postRepository.save(foundPost.get());
        return foundPost;
    }
}
