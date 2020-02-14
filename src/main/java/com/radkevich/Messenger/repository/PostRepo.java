package com.radkevich.Messenger.repository;

import com.radkevich.Messenger.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepo extends JpaRepository<Post, Long> {

    List<Post> findAll();

    Page<Post> findByTag(String tag, Pageable pageable);


}
