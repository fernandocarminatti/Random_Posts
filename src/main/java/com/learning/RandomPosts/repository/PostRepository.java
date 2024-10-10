package com.learning.RandomPosts.repository;

import com.learning.RandomPosts.model.AbstractPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<AbstractPost, String> {
}
