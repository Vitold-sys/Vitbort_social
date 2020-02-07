package com.radkevich.Messenger.service;


import com.radkevich.Messenger.model.User;

import java.util.List;


public interface UserService {

    List<User> getAll();

    User findByUsername(String username);

    User findById(Long id);

    void delete(Long id);
}
