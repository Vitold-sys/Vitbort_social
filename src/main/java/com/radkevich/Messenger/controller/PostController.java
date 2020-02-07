package com.radkevich.Messenger.controller;


import com.fasterxml.jackson.annotation.JsonView;
import com.radkevich.Messenger.model.Post;
import com.radkevich.Messenger.model.Views;
import com.radkevich.Messenger.service.PostService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    @JsonView(Views.IdName.class)
    public Iterable<Post> list() {
        return postService.findAll();
    }

    @GetMapping("{id}")
    @JsonView(Views.Full.class)
    public Post getOne(@PathVariable("id") Post post) {
        return post;
    }

    @PostMapping
    public Post create(@RequestBody Post post) {
        post.setCreationDate(LocalDateTime.now());
        return postService.save(post);
    }

    @PutMapping("{id}")
    public Post update(@PathVariable("id") Post postFromDb, @RequestBody Post post) {
        BeanUtils.copyProperties(post, postFromDb, "id");
        postFromDb.setCreationDate(LocalDateTime.now());
        return postService.save(postFromDb);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Post post) {
        postService.delete(post);
        return new ResponseEntity<>("Post has been deleted", HttpStatus.OK);
    }
}
