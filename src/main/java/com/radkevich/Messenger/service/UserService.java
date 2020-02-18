package com.radkevich.Messenger.service;


import com.radkevich.Messenger.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public interface UserService {

    boolean activateUser(String code);

    User register(User user, MultipartFile file) throws IOException;

    List<User> getAll();

    User findByUsername(String username);

    User findById(Long id);

    void delete(Long id);

    User updateProfile(User user);

    void subscribe(Long id);

    void unsubscribe(Long id);

}
