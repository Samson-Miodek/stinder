package ru.stinder.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Hobby {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false,unique = true)
    private String name;

    @ManyToMany(mappedBy = "hobbies")
    private Set<User> users;

    public Hobby(String name) {
        this.name = name.toLowerCase();
    }

    public void setName(String name) {
        this.name = name.toLowerCase();
    }

    @Override
    public String toString() {
        return "Hobby{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", users=" + users +
                '}';
    }
}
