package com.radkevich.Messenger.controller;


import com.fasterxml.jackson.annotation.JsonView;
import com.radkevich.Messenger.model.Post;
import com.radkevich.Messenger.model.Views;
import com.radkevich.Messenger.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public Iterable<Post> main(@RequestParam(required = false, defaultValue = "") String filter) {
        return postService.filterPost(filter);
    }

    @GetMapping("{id}")
    @JsonView(Views.Full.class)
    public Post getOne(@PathVariable("id") Post post) {
        return post;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    public Post create(@RequestBody Post post) {
        return postService.save(post);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAuthority('USER')")
    public Post update(@PathVariable("id") Post postFromDb, @RequestBody Post post) {
        return postService.save(postFromDb);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<String> delete(@PathVariable("id") Post post) {
        postService.delete(post);
        return new ResponseEntity<>("Post has been deleted", HttpStatus.OK);
    }
}
