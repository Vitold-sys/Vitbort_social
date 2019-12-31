package com.radkevich.Messengers.repository;

import com.radkevich.Messengers.model.Post;
import com.radkevich.Messengers.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepo extends CrudRepository<Post, Long> {
/*    @Query("select new com.radkevich.Messengers.model.dto.PostDto(" +
            "   p, " +
            "   count(pl), " +
            "   sum(case when pl = :user then 1 else 0 end) > 0" +
            ") " +
            "from Post p left join p.likes pl " +
            "group by p")
    List<Post> findAll(@Param("user") User user);*/
    List<Post> findByTag(String tag);
}
