package ru.stinder.repository;

import ru.stinder.entity.Hobby;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HobbyRepository extends JpaRepository<Hobby,Long> {

    boolean existsHobbiesByName(String name);

    Hobby findByName(String hobbyName);
}
