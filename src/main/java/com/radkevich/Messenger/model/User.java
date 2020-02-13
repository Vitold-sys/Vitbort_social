package com.radkevich.Messenger.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "users")
@Data
@EqualsAndHashCode(exclude = {"messages", "subscribers", "subscriptions"})
@ToString(exclude = {"messages", "subscribers", "subscriptions"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(name = "created")
    private LocalDateTime created;

    @LastModifiedDate
    @Column(name = "updated")
    private LocalDateTime updated;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @NotBlank(message = "Please check your username")
    @Column(name = "username")
    private String username;

    @NotBlank(message = "Please check your firstName")
    @Column(name = "first_name")
    private String firstName;

    @NotBlank(message = "Please check your lastName")
    @Column(name = "last_name")
    private String lastName;

    @Email(message = "Please check your email")
    @Column(name = "email")
    private String email;

    @NotBlank(message = "Please check your password")
    @Column(name = "password")
    private String password;

    @NotBlank(message = "Please check your phoneNumber")
    private String phoneNumber;
    private String gender;

    @JsonIgnore
    private String activationCode;
    private String filename;

    @NotBlank(message = "Please check your info")
    @Length(max = 2048, message = "Info too long (more than 2kB)")
    private String info;

    @OneToMany(mappedBy = "autUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Message> messages;

    @ManyToMany
    @JoinTable(
            name = "user_subscriptions",
            joinColumns = {@JoinColumn(name = "channel_id")},
            inverseJoinColumns = {@JoinColumn(name = "subscriber_id")}
    )
    @JsonIgnore
    private Set<User> subscribers = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "user_subscriptions",
            joinColumns = {@JoinColumn(name = "subscriber_id")},
            inverseJoinColumns = {@JoinColumn(name = "channel_id")}
    )
    @JsonIgnore
    private Set<User> subscriptions = new HashSet<>();

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

}
