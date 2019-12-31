package com.radkevich.Messengers.model.dto;

import com.radkevich.Messengers.model.Post;
import com.radkevich.Messengers.model.User;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class PostDto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String postname;
    private String text;
    private String tag;
    private User author;
    private String filename;
    private Long Likes;
    private Boolean meLiked;

    public PostDto(Post post, Long likes, Boolean meLiked) {
        this.id = post.getId();
        Likes = likes;
        this.meLiked = meLiked;
    }

    public String getAuthorName() {
        return PostHelper.getAuthorName(author);
    }

    public Long getId() {
        return id;
    }

    public String getPostname() {
        return postname;
    }

    public String getText() {
        return text;
    }

    public String getTag() {
        return tag;
    }

    public User getAuthor() {
        return author;
    }

    public String getFilename() {
        return filename;
    }

    public Long getLikes() {
        return Likes;
    }

    public Boolean getMeLiked() {
        return meLiked;
    }

    @Override
    public String toString() {
        return "PostDto{" +
                "id=" + id +
                ", author=" + author +
                ", Likes=" + Likes +
                ", meLiked=" + meLiked +
                '}';
    }
}
