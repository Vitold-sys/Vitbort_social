package com.radkevich.Messenger.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.radkevich.Messenger.model.Comment;
import com.radkevich.Messenger.model.Views;
import com.radkevich.Messenger.service.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("comments")
@PreAuthorize("hasAuthority('USER')")
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public Iterable<Comment> main(@RequestParam(required = false, defaultValue = "") String filter) {
        return commentService.filterComment(filter);
    }

    @GetMapping("{id}")
    @JsonView(Views.Full.class)
    public Comment getOne(@PathVariable("id") Long id) {
        return commentService.check(id);
    }

    @PostMapping
    public Comment create(@RequestBody Comment comment) {
        return commentService.save(comment);
    }

    @PutMapping("{id}")
    public ResponseEntity<Comment> update(@PathVariable("id") Long id, @RequestBody Comment comment) {
        return new ResponseEntity<>(commentService.update(id, comment), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Comment comment) {
        commentService.delete(comment);
        return new ResponseEntity<>("Comment has been deleted", HttpStatus.OK);
    }
}
