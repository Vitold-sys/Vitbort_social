package com.radkevich.Messenger.service;


import com.radkevich.Messenger.model.Post;
import com.radkevich.Messenger.repository.PostRepo;
import com.radkevich.Messenger.service.util.FileSaver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class PostService extends FileSaver {
    @Value("${upload.path.posts}")
    private String uploadPathPost;

    @Autowired
    private PostRepo postRepo;

    public Iterable<Post> findAll() {
        Iterable<Post> posts;
        posts = postRepo.findAll();
        return posts;
    }

    public Iterable<Post> filterPost(@RequestParam String filter) {
        Iterable<Post> posts;
        if (!filter.isEmpty()) {
            posts = postRepo.findByTag(filter);
        } else {
            posts = postRepo.findAll();
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

    public Post save(Post post) {
        postRepo.save(post);
        return post;
    }


    public void delete(Post post) {
        postRepo.delete(post);
    }
}
