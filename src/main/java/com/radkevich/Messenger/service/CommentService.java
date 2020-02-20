package com.radkevich.Messenger.service;

import com.radkevich.Messenger.exceptions.NotFoundException;
import com.radkevich.Messenger.model.Comment;
import com.radkevich.Messenger.repository.CommentRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CommentService {
    @Value("${upload.path.comment}")
    private String uploadPath;

    @Value("${url.path}")
    private String urlPath;

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

    public Comment save(Comment comment, MultipartFile file) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        comment.setAuthor(name);
        comment.setCreationDate(LocalDateTime.now());
        saveFile(comment, file);
        commentRepo.save(comment);
        comment.setFileDownloadUri(urlPath + "comments/downloadFile/" + comment.getId() + "/" + comment.getFilename());
        return commentRepo.save(comment);
    }

    public Comment update(Long id, Comment comment, MultipartFile file) throws IOException {
        Comment commentFromDb = commentRepo.findById(id).orElseThrow(() -> new NotFoundException("No comment with such id"));
        saveFile(comment, file);
        BeanUtils.copyProperties(comment, commentFromDb, "id");
        commentFromDb.setCreationDate(LocalDateTime.now());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        commentFromDb.setAuthor(name);
        commentFromDb.setFileDownloadUri(urlPath + "comments/downloadFile/" + commentFromDb.getId() + "/" + commentFromDb.getFilename());
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

    public void saveFile(Comment comment, MultipartFile file) throws IOException {
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" +resultFilename));
            comment.setFilename(resultFilename);
        }
    }

    public Resource loadFileAsResource(Long id) throws MalformedURLException {
        Comment comment = commentRepo.findById(id).orElseThrow(() -> new NotFoundException("No comment with such id"));
        Path filePath =  Paths.get(uploadPath + "/" + comment.getFilename())
                .toAbsolutePath().normalize();
        Resource resource = new UrlResource(filePath.toUri());
        return resource;
    }
}
