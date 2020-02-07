package com.radkevich.Messenger.repository;

import com.radkevich.Messenger.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepo extends JpaRepository<Comment, Long> {

    List<Comment> findByTag(String tag);
}
