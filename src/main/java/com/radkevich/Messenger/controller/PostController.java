package com.radkevich.Messenger.controller;


import com.fasterxml.jackson.annotation.JsonView;
import com.radkevich.Messenger.model.Post;
import com.radkevich.Messenger.model.Views;
import com.radkevich.Messenger.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<List<Post>> main(@RequestParam(required = false, defaultValue = "") String filter,
                                           @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Post> posts = postService.filterPost(filter,pageable);
        return ResponseEntity.ok(posts.getContent());
    }

    @GetMapping("{id}")
    @JsonView(Views.Full.class)
    public Post getOne(@PathVariable("id") Long id) {
        return postService.check(id);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    public Post create(@RequestBody Post post) {
        return postService.save(post);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Post> update(@PathVariable("id") Long id, @RequestBody Post post) {
        return new ResponseEntity<Post>(postService.update(id, post), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<String> delete(@PathVariable("id") Post post) {
        postService.delete(post);
        return new ResponseEntity<>("Post has been deleted", HttpStatus.OK);
    }

    @GetMapping("/{id}/like")
    @PreAuthorize("hasAuthority('USER')")
    public Iterable<Post> like(
            @PathVariable(name = "id") long id
    ) {
        return postService.Like(id);
    }
}
