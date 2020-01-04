package com.radkevich.Messengers.model.dto;

import com.radkevich.Messengers.model.Post;
import com.radkevich.Messengers.model.User;

public class PostDto {
    private Long id;
    private String postname;
    private String text;
    private String tag;
    private User author;
    private String filename;
    private Long likes;
    private Boolean meLiked;

    public PostDto(Post post, Long likes, Boolean meLiked) {
        this.id = post.getId();
        this.postname = post.getPostname();
        this.text = post.getText();
        this.tag = post.getTag();
        this.author = post.getAuthor();
        this.filename = post.getFilename();
        this.likes = likes;
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
        return likes;
    }

    public Boolean getMeLiked() {
        return meLiked;
    }

    @Override
    public String toString() {
        return "PostDto{" +
                "id=" + id +
                ", author=" + author +
                ", Likes=" + likes +
                ", meLiked=" + meLiked +
                '}';
    }
}
