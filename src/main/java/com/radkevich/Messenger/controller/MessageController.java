package com.radkevich.Messenger.controller;


import com.fasterxml.jackson.annotation.JsonView;
import com.radkevich.Messenger.model.Message;
import com.radkevich.Messenger.model.User;
import com.radkevich.Messenger.model.Views;
import com.radkevich.Messenger.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("messages")
@PreAuthorize("hasAuthority('USER')")
public class MessageController {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping
    public Iterable<Message> main(@RequestParam(required = false, defaultValue = "") String filter) {
        return messageService.filterMessage(filter);
    }

    @GetMapping("{id}")
    @JsonView(Views.Full.class)
    public Message getOne(@PathVariable("id") Message message) {
        return message;
    }

    @PostMapping
    public Message create(@RequestBody Message message) {
        return messageService.save(message);
    }

    @PutMapping("{id}")
    public ResponseEntity<Message> update(@PathVariable("id") Message messageFromDb, @RequestBody Message message) {
        messageService.update(messageFromDb, message);
        return new ResponseEntity<>(messageFromDb, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Message message) {
        messageService.delete(message);
        return new ResponseEntity<>("Message has been deleted", HttpStatus.OK);
    }

    @GetMapping("/user-messages/")
    public ResponseEntity<?> userMessages() {
        return new ResponseEntity<>(messageService.userMessages(), HttpStatus.OK);
    }

}
