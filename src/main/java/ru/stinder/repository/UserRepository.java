package ru.stinder.repository;

import ru.stinder.entity.Hobby;
import ru.stinder.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String email);

    User findByActivationCode(String code);

    List<User> findAllByRole(String role);

    List<User> findAllByHobbiesContains(Hobby hobby);

}
