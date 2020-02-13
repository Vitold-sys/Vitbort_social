package com.radkevich.Messenger.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table
@Data
@JsonIgnoreProperties({"filename"})
public class Message {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @JsonView(Views.IdName.class)
    private Long id;

    @Column(updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonView(Views.IdName.class)
    private LocalDateTime creationDate;

    @NotBlank(message = "Please fill the post text")
    @Length(max = 2048, message = "Post too long (more than 2kB)")
    @JsonView(Views.IdName.class)
    private String text;

    @JsonView(Views.IdName.class)
    private String tag;

    @JsonView(Views.IdName.class)
    private String filename;


    @JsonView(Views.Full.class)
    private String author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User autUser;

}