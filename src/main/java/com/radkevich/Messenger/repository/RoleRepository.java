package com.radkevich.Messenger.repository;

import com.radkevich.Messenger.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}