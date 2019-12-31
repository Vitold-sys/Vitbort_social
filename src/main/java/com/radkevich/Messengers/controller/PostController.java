package com.radkevich.Messengers.controller;

import com.radkevich.Messengers.model.Comment;
import com.radkevich.Messengers.model.Post;
import com.radkevich.Messengers.model.User;
import com.radkevich.Messengers.repository.CommentRepo;
import com.radkevich.Messengers.repository.PostRepo;
import com.radkevich.Messengers.service.CommentService;
import com.radkevich.Messengers.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@Controller
public class PostController {
    @Autowired
    private PostService postService;

    @Autowired
    private PostRepo postRepo;

    @GetMapping("/posts")
    public String post(@AuthenticationPrincipal User user, @RequestParam(required = false, defaultValue = "") String filter, Model model) {
        Iterable<Post> posts = postService.filterPost(filter);
        model.addAttribute("posts", posts);
        return "postList";
    }

    @PostMapping("/posts")
    public String addPost(@AuthenticationPrincipal User user, @Valid Post post, @RequestParam("file") MultipartFile file, BindingResult bindingResult, Model model) throws IOException {
        postService.checkPost(user, post, file, bindingResult, model);
        Iterable<Post> posts = postRepo.findAll();
        model.addAttribute("posts", posts);
        return "postList";
    }
}
