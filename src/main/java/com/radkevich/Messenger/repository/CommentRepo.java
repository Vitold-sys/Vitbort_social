package com.radkevich.Messenger.repository;

import com.radkevich.Messenger.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepo extends JpaRepository<Comment, Long> {

    Page<Comment> findByTag(String tag, Pageable pageable);
}
