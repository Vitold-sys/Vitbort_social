package com.radkevich.Messenger.service;


import com.radkevich.Messenger.exceptions.NotFoundException;
import com.radkevich.Messenger.model.Post;
import com.radkevich.Messenger.model.User;
import com.radkevich.Messenger.repository.PostRepo;
import com.radkevich.Messenger.repository.UserRepository;
import com.radkevich.Messenger.service.util.FileSaver;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

@Service
public class PostService extends FileSaver {
    @Value("${upload.path.posts}")
    private String uploadPathPost;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepo postRepo;

    public Iterable<Post> findAll() {
        Iterable<Post> posts;
        posts = postRepo.findAll();
        return posts;
    }

    public Page<Post> filterPost(@RequestParam String filter, Pageable pageable) {
        Page<Post> posts;
        if (!filter.isEmpty()) {
            posts = postRepo.findByTag(filter, pageable);
        } else {
            posts = postRepo.findAll(pageable);
        }
        return posts;
    }

    private void saveFile(@Valid Post post, @RequestParam("file") MultipartFile file) throws IOException {
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPathPost);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPathPost + "/" + resultFilename));
            post.setFilename(resultFilename);
        }
    }

    public Post save(Post post,  MultipartFile file) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        post.setAuthor(name);
        post.setCreationDate(LocalDateTime.now());
        saveFile(post, file);
        postRepo.save(post);
        return post;
    }

    public Post update(Long id, Post post, MultipartFile file) throws IOException {
        Post postFromDb = postRepo.findById(id).orElseThrow(() -> new NotFoundException("No post with such id"));
        saveFile(post, file);
        BeanUtils.copyProperties(post, postFromDb, "id");
        postFromDb.setCreationDate(LocalDateTime.now());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        postFromDb.setAuthor(name);
        postRepo.save(postFromDb);
        return postFromDb;
    }

    public void delete(Post post) {
        postRepo.delete(post);
    }


    public Iterable<Post> Like(long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        User currentUser = userRepository.findByUsername(name);
        Post post = postRepo.findById(id).orElse(null);
        Set<User> likes = post.getLikes();
        if (likes.contains(currentUser)) {
            likes.remove(currentUser);
        } else {
            likes.add(currentUser);
        }
        return Collections.singleton(post);
    }

    public Post check(Long id) {
        Post post = postRepo.findById(id).orElseThrow(() -> new NotFoundException("No post with such id"));
        return post;
    }
}
