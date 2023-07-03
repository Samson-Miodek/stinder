package ru.stinder.entity.messenger;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.stinder.entity.User;

import javax.persistence.*;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne
    private User userFrom;

    @OneToOne
    private User userTo;

    @ManyToOne
    private Chat chat;

    private String text;

    private Timestamp date;

    public Message(User userFrom, User userTo, Chat chat, String text) {
        this.userFrom = userFrom;
        this.userTo = userTo;
        this.chat = chat;
        this.text = text;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", userFrom=" + userFrom +
                ", userTo=" + userTo +
                ", chat=" + chat +
                ", text='" + text + '\'' +
                ", date=" + date +
                '}';
    }
}
