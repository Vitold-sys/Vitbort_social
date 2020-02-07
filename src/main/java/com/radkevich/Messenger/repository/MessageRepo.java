package com.radkevich.Messenger.repository;

import com.radkevich.Messenger.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepo extends JpaRepository<Message, Long> {

    List<Message> findByTag(String tag);

}