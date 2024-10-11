package com.learning.RandomPosts.repository;

import com.learning.RandomPosts.model.AbstractPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<AbstractPost, String> {
    Optional<AbstractPost> findByTitleIgnoreCase(String title);
}
