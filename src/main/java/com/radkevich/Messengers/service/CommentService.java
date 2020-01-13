package com.radkevich.Messengers.service;

import com.radkevich.Messengers.controller.ControllerUtils;
import com.radkevich.Messengers.model.Comment;
import com.radkevich.Messengers.model.Message;
import com.radkevich.Messengers.model.User;
import com.radkevich.Messengers.repository.CommentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Map;

@Service
public class CommentService {
    @Autowired
    private CommentRepo commentRepo;


    public Iterable<Comment> findAll() {
        Iterable<Comment> comments;
        comments = commentRepo.findAll();
        return comments;
    }

    public Iterable<Comment> filterComment(@RequestParam String filter) {
        Iterable<Comment> comments;
        if (!filter.isEmpty()) {
            comments = commentRepo.findByTag(filter);
        } else {
            comments = commentRepo.findAll();
        }
        return comments;
    }

    public void checkComment(@AuthenticationPrincipal User user, @Valid Comment comment, BindingResult bindingResult, Model model) {
        comment.setAuthor(user);
        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errorsMap);
            model.addAttribute("comments", comment);
        } else {
            commentRepo.save(comment);
        }
    }
}