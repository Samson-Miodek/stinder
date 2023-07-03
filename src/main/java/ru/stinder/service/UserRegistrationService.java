package ru.stinder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.stinder.config.security.SecurityConfiguration;
import ru.stinder.dto.UserRegistrationDTO;
import ru.stinder.entity.User;
import ru.stinder.entity.security.Authority;
import ru.stinder.repository.UserRepository;

import java.util.Collections;
import java.util.UUID;

@Service
public class UserRegistrationService {
    @Autowired
    private UserRepository userRepository;

    public void newUserRegistration(UserRegistrationDTO userRegistrationDTO){
        var newUser = new User(userRegistrationDTO);
        //newUser.setActive(true);
        newUser.setAuthorities(Collections.singleton(Authority.USER));
        newUser.setPassword(SecurityConfiguration.passwordEncoder().encode(userRegistrationDTO.getPassword()));
        newUser.setActivationCode(UUID.randomUUID().toString());
        newUser.setRole("Студент");
        newUser.setActive(true);
        userRepository.save(newUser);
    }

}
