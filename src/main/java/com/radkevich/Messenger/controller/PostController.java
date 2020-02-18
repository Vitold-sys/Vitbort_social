package com.radkevich.Messenger.controller;


import com.fasterxml.jackson.annotation.JsonView;
import com.radkevich.Messenger.model.Post;
import com.radkevich.Messenger.model.Views;
import com.radkevich.Messenger.service.PostService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:8090")
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
        Page<Post> posts = postService.filterPost(filter, pageable);
        return ResponseEntity.ok(posts.getContent());
    }

    @GetMapping("{id}")
    public Post getOne(@ApiParam(value = "ID of post to return", required = true)
                       @PathVariable("id") Long id) {
        return postService.check(id);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    public Post create(@RequestPart Post post, @RequestPart("file") MultipartFile file) throws IOException {
        return postService.save(post, file);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Post> update(@ApiParam(value = "ID of post to return", required = true)
                                       @PathVariable("id") Long id, @RequestPart Post post,
                                       @RequestPart("file") MultipartFile file) throws IOException {
        return new ResponseEntity<Post>(postService.update(id, post, file), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<String> delete(@ApiParam(value = "ID of post to return", required = true)
                                         @PathVariable("id") Post post) {
        postService.delete(post);
        return new ResponseEntity<>("Post has been deleted", HttpStatus.OK);
    }

    @GetMapping("/{id}/like")
    @PreAuthorize("hasAuthority('USER')")
    public Iterable<Post> like(@ApiParam(value = "ID of post to return", required = true)
                               @PathVariable(name = "id") long id
    ) {
        return postService.Like(id);
    }
}
