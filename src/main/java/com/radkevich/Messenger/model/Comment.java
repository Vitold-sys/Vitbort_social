package com.radkevich.Messenger.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.net.URI;
import java.net.URL;
import java.time.LocalDateTime;

@Entity
@Table
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(Views.IdName.class)
    private Long id;

    @Column(updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonView(Views.Full.class)
    private LocalDateTime creationDate;

    @NotBlank(message = "Please check your comment text")
    @Length(max = 2048, message = "Post too long (more than 2kB)")
    @JsonView(Views.IdName.class)
    private String text;

    @JsonView(Views.IdName.class)
    private String tag;

    private String author;

    private String fileDownloadUri;

    private String filename;
}

