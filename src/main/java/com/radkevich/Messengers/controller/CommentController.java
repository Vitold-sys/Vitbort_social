package com.radkevich.Messengers.controller;


import com.radkevich.Messengers.model.Comment;
import com.radkevich.Messengers.model.User;
import com.radkevich.Messengers.repository.CommentRepo;
import com.radkevich.Messengers.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Map;

@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepo commentRepo;

    @GetMapping("/comment")
    public String main(@RequestParam(required = false, defaultValue = "") String filter, Model model) {
        Iterable<Comment> comments = commentService.filterComment(filter);
        model.addAttribute("comments", comments);
        return "comment";
    }

    @PostMapping("/comment")
    public String addComment(@AuthenticationPrincipal User user, @Valid Comment comment, BindingResult bindingResult, Model model) {
        commentService.checkComment(user, comment, bindingResult, model);
        Iterable<Comment> comments = commentRepo.findAll();
        model.addAttribute("comments", comments);
        return "comment";
    }
}