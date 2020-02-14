package com.radkevich.Messenger.service;


import com.radkevich.Messenger.exceptions.NotFoundException;
import com.radkevich.Messenger.filter.CustomRsqlVisitor;
import com.radkevich.Messenger.model.Message;
import com.radkevich.Messenger.model.User;
import com.radkevich.Messenger.repository.MessageRepo;
import com.radkevich.Messenger.repository.UserRepository;
import com.radkevich.Messenger.service.util.FileSaver;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
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

    public Page<Message> filterMessage(@RequestParam String filter, Pageable pageable) {
        Page<Message> messages;
        if (!filter.isEmpty()) {
            Node rootNode = new RSQLParser().parse(filter);
            Specification<Message> spec = rootNode.accept(new CustomRsqlVisitor<Message>());
            messages =  messageRepo.findAll(spec, pageable);
        } else {
            messages = messageRepo.findAll(pageable);
        }
        return messages;
    }

    public void saveFile(Message message, MultipartFile file) throws IOException {
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
        Message message = messageRepo.findById(id).orElseThrow(() -> new NotFoundException("No message with such id"));
        return message;
    }

    public Message update(Long id, Message message) {
        Message messageFromDb = messageRepo.findById(id).orElseThrow(() -> new NotFoundException("No message with such id"));
        BeanUtils.copyProperties(message, messageFromDb, "id");
        messageFromDb.setCreationDate(LocalDateTime.now());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        messageFromDb.setAuthor(name);
        messageRepo.save(messageFromDb);
        return messageFromDb;
    }

    public Set<Message> userMessages() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        User userCurrent = userRepository.findByUsername(name);
        Set<Message> messages = userCurrent.getMessages();
        return messages;
    }
}