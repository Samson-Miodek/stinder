package com.example.demo.repository;

import com.example.demo.entity.Hobby;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String email);

    User findByActivationCode(String code);

    List<User> findAllByRole(String role);

    List<User> findAllByHobbiesContains(Hobby hobby);

}
