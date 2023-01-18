package com.example.demo.repository;

import com.example.demo.entity.Chat;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface ChatRepository extends JpaRepository<Chat,Long> {
    Chat findByInviteCode(String code);

}
