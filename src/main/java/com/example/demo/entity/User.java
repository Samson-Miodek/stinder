package com.example.demo.entity;

import com.example.demo.entity.security.Authority;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "app_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    @Size(min = 1, max = 20)
    private String name;

    @Column(nullable = false)
    @Size(min = 1, max = 20)
    private String surname;

    @Column
    private String patronymic;

    @Column
    private String specialty;

    @Column
    private String areaScientificInterests;

    @Column
    private String academicTitle;

    @Column
    private String academicDegree;

    @Column
    private String position;
    @Column
    private String role;
    @Column
    private String about;

    @Column
    private String status;

    @Column(nullable = true)
    private Date birthday;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    @Size(min = 3, max = 30)
    @Setter(AccessLevel.NONE)
    private String email;
    @Column
    private String activationCode;
    @Column
    private boolean active;
    @ElementCollection(targetClass = Authority.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_authorities", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Authority> authorities;


    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_chat",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "chat_id")}
    )
    private Set<Chat> chats;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_dialogue",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "dialogue_id")}
    )
    private Set<Dialogue> dialogues;


    @ManyToMany
    @JoinTable(
            name = "user_hobby",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "hobby_id")}
    )
    private Set<Hobby> hobbies;


    @OneToMany
    @JoinColumn(name = "user_id")
    private List<Publication> publications;

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    public void setRole(String role) {
        if (role.equals("Студент") || role.equals("Студент-аспирант") || role.equals("Научный сотрудник") || role.equals("Научный руководитель")) {
            this.role = role;
        } else
            this.role = "Студент";
    }

    public void setStatus(String status) {
        if (status.equals("Не ищу команду") || status.equals("Ищу команду") || status.equals("Ищу проект")) {
            this.status = status;
        } else
            this.status = "Не ищу команду";
    }

    public void setAbout(String about) {
        this.about = about;
        if (about.length() > 50)
            this.about = about.substring(0, 50);
    }

    public boolean isModerator() {
        return authorities.contains(Authority.MODERATOR);
    }
}