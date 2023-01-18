package com.example.demo.repository;

import com.example.demo.entity.Hobby;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HobbyRepository extends JpaRepository<Hobby,Long> {

    boolean existsHobbiesByName(String name);

    Hobby findByName(String hobbyName);
}
