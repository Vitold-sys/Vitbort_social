package com.radkevich.Messenger.service;


import com.radkevich.Messenger.exceptions.NotFoundException;
import com.radkevich.Messenger.model.Message;
import com.radkevich.Messenger.model.User;
import com.radkevich.Messenger.repository.MessageRepo;
import com.radkevich.Messenger.repository.UserRepository;
import com.radkevich.Messenger.service.util.FileSaver;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;


@Service
public class MessageService extends FileSaver {
    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepo messageRepo;

    public Iterable<Message> findAll() {
        Iterable<Message> messages;
        messages = messageRepo.findAll();
        return messages;
    }

    public Iterable<Message> filterMessage(@RequestParam String filter) {
        Iterable<Message> messages;
        if (!filter.isEmpty()) {
            messages = messageRepo.findByTag(filter);
        } else {
            messages = messageRepo.findAll();
        }
        return messages;
    }

    public void saveFile(@Valid Message message, @RequestParam("file") MultipartFile file) throws IOException {
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + resultFilename));
            message.setFilename(resultFilename);
        }
    }

    public Message save(Message message) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        message.setAuthor(name);
        message.setCreationDate(LocalDateTime.now());
        messageRepo.save(message);
        return message;
    }

    public void delete(Message message) {
        messageRepo.delete(message);
    }

    public Message check(Long id) {
        Message message = messageRepo.findById(id).orElseThrow(()-> new NotFoundException("No message"));
        return message;
    }

    public Message update(Message messageFromDb, Message message) {
        BeanUtils.copyProperties(message, messageFromDb, "id");
        messageFromDb.setCreationDate(LocalDateTime.now());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        messageFromDb.setAuthor(name);
        messageRepo.save(messageFromDb);
        return messageFromDb;
    }

    public Set<Message> userMessages(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        User userCurrent = userRepository.findByUsername(name);
        Set<Message> messages = userCurrent.getMessages();
        return messages;
    }
}