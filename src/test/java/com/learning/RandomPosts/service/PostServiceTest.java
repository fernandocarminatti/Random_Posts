package com.learning.RandomPosts.service;

import com.learning.RandomPosts.dto.NewPostDto;
import com.learning.RandomPosts.dto.PostUpdateDto;
import com.learning.RandomPosts.model.AbstractPost;
import com.learning.RandomPosts.model.PostFactory;
import com.learning.RandomPosts.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    PostRepository postRepository;

    @InjectMocks
    PostService postService;

    @DisplayName("Return all posts from repository")
    @Test
    void testGetAllPosts() {
        MultipartFile dummyFile = mock(MultipartFile.class);
        AbstractPost post001 = PostFactory.createPostByType(new NewPostDto(1, "Post 001", "Post 001 Content", "Test Author", List.of(dummyFile)), "Dummy Attachment Path");
        post001.setId("ID - Post 001");
        AbstractPost post002 = PostFactory.createPostByType(new NewPostDto(2, "Post 002", "Post 002 Content", "Test Author", List.of(dummyFile)), "Dummy Attachment Path");
        post002.setId("ID - Post 002");
        AbstractPost post003 = PostFactory.createPostByType(new NewPostDto(3, "Post 003", "Post 003 Content", "Test Author", List.of(dummyFile)), "Dummy Attachment Path");
        post003.setId("ID - Post 003");

        when(postRepository.findAll()).thenReturn(List.of(post001, post002, post003));

        assertEquals(3, postService.getAllPosts().size());
    }

    @DisplayName("Return post by title")
    @ParameterizedTest
    @CsvSource({"Post 001", "Post 002", "Post 003"})
    void testGetPostByTitle(String title) {
        MultipartFile dummyFile = mock(MultipartFile.class);
        NewPostDto postDto = new NewPostDto(1, title, "Post 001 Content", "Test Author", List.of(dummyFile));
        AbstractPost dummyPost = PostFactory.createPostByType(postDto, "Dummy Attachment Path");
        when(postRepository.findByTitleIgnoreCase(title)).thenReturn(Optional.of(dummyPost));

        Optional<AbstractPost> testPost = postService.getPostByTitle(title);

        assertTrue(testPost.isPresent());
        assertEquals(dummyPost.getTitle(), testPost.get().getTitle());
    }

    @DisplayName("Create new post")
    @ParameterizedTest
    @CsvSource({"Post 004, 1", "Post 005, 2", "Post 006, 3"})
    void testCreatePost(String title, Integer postType) {
        MultipartFile dummyFile = mock(MultipartFile.class);
        NewPostDto postDto = new NewPostDto(postType, title, "Dummy Content", "Test Author", List.of(dummyFile));

        when(postRepository.save(any(AbstractPost.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<AbstractPost> testPost = postService.createPost(postDto);

        assertTrue(testPost.isPresent());
        assertEquals(title, testPost.get().getTitle());
    }

    @DisplayName("Conflict on post creation | Exception thrown")
    @ParameterizedTest
    @CsvSource({"Post 004, 1", "Post 005, 2", "Post 006, 3"})
    void testCreatePostConflict(String title, Integer postType) {
        MultipartFile dummyFile = mock(MultipartFile.class);
        NewPostDto postDto = new NewPostDto(postType, title, "Dummy Content", "Test Author", List.of(dummyFile));
        when(postRepository.save(any(AbstractPost.class))).thenThrow(DataIntegrityViolationException.class);

        assertThrows(DataIntegrityViolationException.class, () -> postService.createPost(postDto));
    }

    @DisplayName("Return TRUE for a post that exist")
    @Test
    void testDeletePost() {
        MultipartFile dummyFile = mock(MultipartFile.class);
        NewPostDto postDto = new NewPostDto(1, "Dummy Title 001", "Post 001 Content", "Test Author", List.of(dummyFile));
        AbstractPost dummyPost = PostFactory.createPostByType(postDto, "Dummy Attachment Path");
        when(postRepository.findByTitleIgnoreCase(any(String.class))).thenReturn(Optional.of(dummyPost));
        doNothing().when(postRepository).delete(any(AbstractPost.class));

        boolean testResult = postService.deletePost("Dummy Title 001");

        assertTrue(testResult);
    }

    @DisplayName("Return FALSE for a post that doesn't exist")
    @Test
    void testDeletePostNotFound() {

        when(postRepository.findByTitleIgnoreCase(any(String.class))).thenReturn(Optional.empty());

        boolean testResult = postService.deletePost("Dummy Title 001");

        assertFalse(testResult);
    }

    @DisplayName("Update title and content of a existing post")
    @ParameterizedTest
    @CsvSource({"Post 001, 1, 001 Title", "Post 002, 2, 002 Title", "Post 003, 3, 003 Title"})
    void testUpdatePostData(String title, Integer postType, String newTitle) {
        MultipartFile dummyFile = mock(MultipartFile.class);
        NewPostDto postDto = new NewPostDto(postType, title, "Dummy Content", "Test Author", List.of(dummyFile));
        PostUpdateDto updatedData = new PostUpdateDto(newTitle, "Updated Dummy Content");
        AbstractPost dummyPost = PostFactory.createPostByType(postDto, "Dummy Attachment Path");
        when(postRepository.findByTitleIgnoreCase(any(String.class))).thenReturn(Optional.of(dummyPost));
        when(postRepository.save(any(AbstractPost.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<AbstractPost> testPost = postService.updatePostData(updatedData, title);

        assertTrue(testPost.isPresent());
        assertEquals(updatedData.content(), testPost.get().getContent());
        assertEquals(newTitle, testPost.get().getTitle());
    }

    @DisplayName("Optional.empty() returned if post doesn't exist")
    @ParameterizedTest
    @CsvSource({"Post 001, 1, 001 Title", "Post 002, 2, 002 Title", "Post 003, 3, 003 Title"})
    void testUpdatePostDataNotFound(String title, Integer postType, String newTitle) {
        MultipartFile dummyFile = mock(MultipartFile.class);
        NewPostDto postDto = new NewPostDto(postType, title, "Dummy Content", "Test Author", List.of(dummyFile));
        PostUpdateDto updatedData = new PostUpdateDto(newTitle, "Updated Dummy Content");
        AbstractPost dummyPost = PostFactory.createPostByType(postDto, "Dummy Attachment Path");
        when(postRepository.findByTitleIgnoreCase(any(String.class))).thenReturn(Optional.empty());

        Optional<AbstractPost> testPost = postService.updatePostData(updatedData, title);

        assertInstanceOf(Optional.class, testPost);
        assertTrue(testPost.isEmpty());
    }
}