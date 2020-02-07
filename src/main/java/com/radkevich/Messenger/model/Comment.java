package com.radkevich.Messenger.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @JsonView(Views.IdName.class)
    private Long id;

    @Column(updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonView(Views.Full.class)
    private LocalDateTime creationDate;

    @JsonView(Views.IdName.class)
    private String text;

    @JsonView(Views.IdName.class)
    private String tag;
}

