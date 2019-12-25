package com.radkevich.Messengers.controller;

import com.radkevich.Messengers.model.Message;
import com.radkevich.Messengers.model.User;
import com.radkevich.Messengers.repository.MessageRepo;
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
import java.awt.print.Book;
import java.io.IOException;
import java.util.Map;

@Controller
public class MessageController {
        @Autowired
        private MessageRepo messageRepo;

        @GetMapping("/")
        public String greeting(Map<String, Object> model) {
            return "greeting";
        }

        @GetMapping("/main")
        public String main(@RequestParam(required = false, defaultValue = "") String filter, Model model) {
            Iterable<Message> messages = messageRepo.findAll();

            if (filter != null && !filter.isEmpty()) {
                messages = messageRepo.findByTag(filter);
            } else {
                messages = messageRepo.findAll();
            }

            model.addAttribute("messages", messages);
            model.addAttribute("filter", filter);

            return "main";
        }

        @PostMapping("/main")
        public String add(
                @AuthenticationPrincipal User user,
                @Valid Message message,
                BindingResult bindingResult,
                Model model,
                @RequestParam("file") MultipartFile file
        ) throws IOException {
            message.setAuthor(user);

            if (bindingResult.hasErrors()) {
                Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);

                model.mergeAttributes(errorsMap);
                model.addAttribute("message", message);
            } else {

                model.addAttribute("message", null);

                messageRepo.save(message);
            }

            Iterable<Message> messages = messageRepo.findAll();

            model.addAttribute("messages", messages);

            return "main";
        }
    }