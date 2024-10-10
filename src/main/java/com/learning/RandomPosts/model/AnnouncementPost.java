package com.learning.RandomPosts.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ANNOUNCEMENT")
public class AnnouncementPost extends AbstractPost {
    public AnnouncementPost() {
    }

    public AnnouncementPost(String title, String content, String author, String attachment) {
        super(title, content, author, attachment);
    }
}
