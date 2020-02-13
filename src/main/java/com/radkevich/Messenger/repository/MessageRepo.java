package com.radkevich.Messenger.repository;


import com.radkevich.Messenger.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepo extends CrudRepository<Message, Long> {

    Page<Message> findAll(Pageable pageable);

    Page<Message> findByTag(String tag, Pageable pageable);

}