package com.radkevich.Messenger.repository;

import com.radkevich.Messenger.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepo extends JpaRepository<Post, Long> {

    List<Post> findAll();

    List<Post> findByTag(String tag);


}
