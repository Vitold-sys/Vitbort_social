package com.radkevich.Messenger.service;



import com.radkevich.Messenger.model.Message;
import com.radkevich.Messenger.repository.MessageRepo;
import com.radkevich.Messenger.service.util.FileSaver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.UUID;


@Service
public class MessageService extends FileSaver {
    @Value("${upload.path}")
    private String uploadPath;

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
        messageRepo.save(message);
        return message;
    }

    public void delete(Message message) {
        messageRepo.delete(message);
    }
}