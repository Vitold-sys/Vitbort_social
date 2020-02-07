package com.radkevich.Messenger.controller;


import com.fasterxml.jackson.annotation.JsonView;
import com.radkevich.Messenger.model.Message;
import com.radkevich.Messenger.model.Views;
import com.radkevich.Messenger.service.MessageService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("messages")
//@PreAuthorize(value="hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
//@PreAuthorize(value="hasRole('ADMIN') or hasRole('USER')")
@PreAuthorize("hasAuthority('ADMIN')")
public class MessageController {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping
    @JsonView(Views.IdName.class)
    public Iterable<Message> list() {
        return messageService.findAll();
    }

    @GetMapping("{id}")
    @JsonView(Views.Full.class)
    public Message getOne(@PathVariable("id") Message message) {
        return message;
    }

    @PostMapping
    public Message create(@RequestBody Message message) {
        message.setCreationDate(LocalDateTime.now());
        return messageService.save(message);
    }

    @PutMapping("{id}")
    public Message update(@PathVariable("id") Message messageFromDb, @RequestBody Message message) {
        BeanUtils.copyProperties(message, messageFromDb, "id");
        messageFromDb.setCreationDate(LocalDateTime.now());
        return messageService.save(messageFromDb);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Message message) {
        messageService.delete(message);
        return new ResponseEntity<>("Message has been deleted", HttpStatus.OK);
    }
}
