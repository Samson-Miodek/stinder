package com.example.demo.repository;

import com.example.demo.entity.Dialogue;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DialogueRepository extends JpaRepository<Dialogue,Long> {

    Dialogue findByUserfromAndUsertoOrUsertoAndUserfrom(User from1, User to1, User from2, User to2);

}
