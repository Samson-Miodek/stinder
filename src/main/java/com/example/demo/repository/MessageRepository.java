package com.example.demo.repository;

import com.example.demo.entity.Message;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository  extends JpaRepository<Message,Long> {

//    Message findMessageByUserFromAndUserTo(User from, User to);

    Message findFirstByUserFromAndUserTo(User from, User to);

    List<Message> findAllByUserFromAndUserTo(User from, User to);
    List<Message> findAllByUserFromAndUserToOrUserToAndUserFrom(User from1, User to1,User to2, User from2);

}
