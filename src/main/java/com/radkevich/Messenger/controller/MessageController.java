package com.radkevich.Messenger.controller;


import com.fasterxml.jackson.annotation.JsonView;
import com.radkevich.Messenger.model.Message;
import com.radkevich.Messenger.model.Views;
import com.radkevich.Messenger.service.MessageService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin(origins = "http://localhost:8090")
@RequestMapping("messages")
@PreAuthorize("hasAuthority('USER')")
public class MessageController {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping
    public ResponseEntity<Iterable<Message>> main(@RequestParam(required = false, defaultValue = "") String filter,
                                                  @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Message> messages = messageService.filterMessage(filter, pageable);
        return ResponseEntity.ok(messages.getContent());
    }

    @GetMapping("{id}")
    @JsonView(Views.Full.class)
    public Message getOne(@ApiParam(value = "ID of message to return", required = true)
                          @PathVariable("id") Long id) {
        return messageService.check(id);
    }

    @PostMapping
    @JsonView(Views.Full.class)
    public Message create(@RequestPart Message message,
                          @RequestPart("file") MultipartFile file) throws IOException {
        return messageService.save(message, file);
    }

    @PutMapping("{id}")
    @JsonView(Views.Full.class)
    public ResponseEntity<Message> update(@ApiParam(value = "ID of message to return", required = true)
                                          @PathVariable("id") Long id,
                                          @RequestPart Message message,
                                          @RequestPart("file") MultipartFile file) throws IOException {
        return new ResponseEntity<>(messageService.update(id, message, file), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@ApiParam(value = "ID of message to return", required = true)
                                         @PathVariable("id") Message message) {
        messageService.delete(message);
        return new ResponseEntity<>("Message has been deleted", HttpStatus.OK);
    }

    @GetMapping("/user-messages/")
    @JsonView(Views.IdName.class)
    public ResponseEntity<?> userMessages() {
        return new ResponseEntity<>(messageService.userMessages(), HttpStatus.OK);
    }

}
