package ru.stinder.entity.messenger;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.stinder.entity.User;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    @ManyToMany(mappedBy = "chats")
    private Set<User> users;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
    private List<Message> messages;

    @OneToOne(fetch = FetchType.EAGER)
    private Message lastMessage;

    private String inviteCode;

    @Override
    public String toString() {
        return "Chat{" +
                "id=" + id +
                ", users=" + users +
                ", messages=" + messages +
                '}';
    }
}
