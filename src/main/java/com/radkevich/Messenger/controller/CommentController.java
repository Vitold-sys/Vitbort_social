package com.radkevich.Messenger.controller;

import com.radkevich.Messenger.model.Comment;
import com.radkevich.Messenger.service.CommentService;
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
@RequestMapping("comments")
@PreAuthorize("hasAuthority('USER')")
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public ResponseEntity<List<Comment>> main(@RequestParam(required = false, defaultValue = "") String filter,
                                              @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Comment> comments = commentService.filterComment(filter, pageable);
        return ResponseEntity.ok(comments.getContent());
    }

    @GetMapping("{id}")
    public Comment getOne(@ApiParam(value = "ID of comment to return", required = true) @PathVariable("id") Long id) {
        return commentService.check(id);
    }

    @PostMapping
    public Comment create(@RequestPart Comment comment, @RequestPart("file") MultipartFile file) throws IOException {
        return commentService.save(comment, file);
    }

    @PutMapping("{id}")
    public ResponseEntity<Comment> update(@ApiParam(value = "ID of comment to return", required = true)
                                          @PathVariable("id") Long id, @RequestPart Comment comment,
                                          @RequestPart("file") MultipartFile file) throws IOException {
        return new ResponseEntity<>(commentService.update(id, comment, file), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@ApiParam(value = "ID of comment to return", required = true)
                                         @PathVariable("id") Comment comment) {
        commentService.delete(comment);
        return new ResponseEntity<>("Comment has been deleted", HttpStatus.OK);
    }

    @GetMapping("/downloadFile/{id}/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id, HttpServletRequest request) throws MalformedURLException {
        Resource resource = commentService.loadFileAsResource(id);
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
