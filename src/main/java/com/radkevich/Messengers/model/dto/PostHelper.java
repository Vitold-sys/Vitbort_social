package com.radkevich.Messengers.model.dto;

import com.radkevich.Messengers.model.User;

public abstract class PostHelper {
    public static String getAuthorName(User author){
        return author != null ? author.getUsername() : "<none>";
    }
}
