package com.radkevich.Messenger.service;

import com.radkevich.Messenger.model.Comment;
import com.radkevich.Messenger.repository.CommentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class CommentService {
    @Autowired
    private CommentRepo commentRepo;

    public Iterable<Comment> findAll() {
        Iterable<Comment> comments;
        comments = commentRepo.findAll();
        return comments;
    }

    public Iterable<Comment> filterComment(@RequestParam String filter) {
        Iterable<Comment> comments;
        if (!filter.isEmpty()) {
            comments = commentRepo.findByTag(filter);
        } else {
            comments = commentRepo.findAll();
        }
        return comments;
    }

    public Comment save(Comment comment) {
        return commentRepo.save(comment);
    }

    public void delete(Comment comment) {
        commentRepo.delete(comment);
    }
}
