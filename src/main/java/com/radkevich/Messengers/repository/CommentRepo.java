package com.radkevich.Messengers.repository;

import com.radkevich.Messengers.model.Comment;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentRepo extends CrudRepository<Comment, Long> {

    List<Comment> findByTag(String tag);

}