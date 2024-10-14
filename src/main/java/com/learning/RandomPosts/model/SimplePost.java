package com.learning.RandomPosts.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("SIMPLE")
public class SimplePost extends AbstractPost {
    public SimplePost() {
    }

    public SimplePost(String title, String content, String author) {
        super(title, content, author);
    }
}
