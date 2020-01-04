package com.radkevich.Messengers.controller;

import com.radkevich.Messengers.model.Post;
import com.radkevich.Messengers.model.User;
import com.radkevich.Messengers.model.dto.PostDto;
import com.radkevich.Messengers.repository.PostRepo;
import com.radkevich.Messengers.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Set;

@Controller
public class PostController {
    @Autowired
    private PostService postService;

    @Autowired
    private PostRepo postRepo;

    @GetMapping("/posts")
    public String post(@AuthenticationPrincipal User user, @RequestParam(required = false, defaultValue = "") String filter, Model model) {
        Iterable<PostDto> posts = postService.filterPost(filter, user);
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

    @GetMapping("/posts/{post}/like")
    public String like(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Post post,
            RedirectAttributes redirectAttributes,
            @RequestHeader(required = false) String referer
    ) {
        Set<User> likes = post.getLikes();
        if (likes.contains(currentUser)) {
            likes.remove(currentUser);
        } else {
            likes.add(currentUser);
        }
        UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();
        components.getQueryParams()
                .entrySet()
                .forEach(pair -> redirectAttributes.addAttribute(pair.getKey(), pair.getValue()));
        return "redirect:" + components.getPath();
    }
}
