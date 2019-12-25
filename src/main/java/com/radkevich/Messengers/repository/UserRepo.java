package com.radkevich.Messengers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.radkevich.Messengers.model.User;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findByActivationCode(String code);
}