package com.radkevich.Messenger.service;

import com.radkevich.Messenger.exceptions.NotFoundException;
import com.radkevich.Messenger.model.Comment;
import com.radkevich.Messenger.repository.CommentRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Service
public class CommentService {
    @Autowired
    private CommentRepo commentRepo;

    public Iterable<Comment> findAll() {
        Iterable<Comment> comments;
        comments = commentRepo.findAll();
        return comments;
    }

    public Page<Comment> filterComment(@RequestParam String filter, Pageable pageable) {
        Page<Comment> comments;
        if (!filter.isEmpty()) {
            comments = commentRepo.findByTag(filter, pageable);
        } else {
            comments = commentRepo.findAll(pageable);
        }
        return comments;
    }

    public Comment save(Comment comment) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        comment.setAuthor(name);
        comment.setCreationDate(LocalDateTime.now());
        return commentRepo.save(comment);
    }

    public Comment update(Long id, Comment comment) {
        Comment commentFromDb = commentRepo.findById(id).orElseThrow(() -> new NotFoundException("No comment with such id"));
        BeanUtils.copyProperties(comment, commentFromDb, "id");
        commentFromDb.setCreationDate(LocalDateTime.now());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        commentFromDb.setAuthor(name);
        commentRepo.save(commentFromDb);
        return commentFromDb;
    }

    public void delete(Comment comment) {
        commentRepo.delete(comment);
    }


    public Comment check(Long id) {
        Comment comment = commentRepo.findById(id).orElseThrow(() -> new NotFoundException("No comment with such id"));
        return comment;
    }

}
