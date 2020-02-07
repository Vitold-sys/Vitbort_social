package com.radkevich.Messenger.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table
@Data
public class Post {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @JsonView(Views.IdName.class)
    private Long id;

    @JsonView(Views.IdName.class)
    private String postname;

    @Column(updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonView(Views.Full.class)
    private LocalDateTime creationDate;

    @JsonView(Views.IdName.class)
    private String text;

    @JsonView(Views.IdName.class)
    private String tag;

    @JsonIgnore(value = false)
    private String filename;


}