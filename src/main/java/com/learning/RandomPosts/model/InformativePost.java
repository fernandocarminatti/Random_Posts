package com.learning.RandomPosts.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("INFORMATIVE")
public class InformativePost extends AbstractPost {
    public InformativePost() {
    }

    public InformativePost(String title, String content, String author, String attachment) {
        super(title, content, author, attachment);
    }
}
