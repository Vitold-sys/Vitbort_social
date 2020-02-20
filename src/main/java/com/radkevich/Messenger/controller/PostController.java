package com.radkevich.Messenger.controller;


import com.radkevich.Messenger.model.Post;
import com.radkevich.Messenger.service.PostService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;
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
    public int like(@ApiParam(value = "ID of post to return", required = true)
                               @PathVariable(name = "id") long id
    ) {
        postService.Like(id);
        Post post = postService.check(id);
        return post.getLikes().size();
    }

    @GetMapping("/downloadFile/{id}/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id, HttpServletRequest request) throws MalformedURLException {
        Resource resource = postService.loadFileAsResource(id);
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
        }
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
