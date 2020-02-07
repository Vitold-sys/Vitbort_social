package com.radkevich.Messenger.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.radkevich.Messenger.model.Comment;
import com.radkevich.Messenger.model.Views;
import com.radkevich.Messenger.service.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("comments")
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    @JsonView(Views.IdName.class)
    public Iterable<Comment> list() {
        return commentService.findAll();
    }

    @GetMapping("{id}")
    @JsonView(Views.Full.class)
    public Comment getOne(@PathVariable("id") Comment comment) {
        return comment;
    }

    @PostMapping
    public Comment create(@RequestBody Comment comment) {
        comment.setCreationDate(LocalDateTime.now());
        return commentService.save(comment);
    }

    @PutMapping("{id}")
    public Comment update(@PathVariable("id") Comment commentFromDb, @RequestBody Comment comment) {
        BeanUtils.copyProperties(comment, commentFromDb, "id");
        commentFromDb.setCreationDate(LocalDateTime.now());
        return commentService.save(commentFromDb);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Comment comment) {
        commentService.delete(comment);
        return new ResponseEntity<>("Comment has been deleted", HttpStatus.OK);
    }
}
