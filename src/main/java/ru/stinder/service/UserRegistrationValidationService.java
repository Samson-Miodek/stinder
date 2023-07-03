package ru.stinder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.stinder.dto.UserRegistrationDTO;
import ru.stinder.repository.UserRepository;

@Service
public class UserRegistrationValidationService {

    @Autowired
    private UserRepository userRepository;

    public static boolean isUserRegistrationDTOcorrect(UserRegistrationDTO userRegistrationDTO) {
        return userRegistrationDTO.getEmail() != null && !userRegistrationDTO.getEmail().isEmpty()
                && userRegistrationDTO.getName() != null && !userRegistrationDTO.getName().isEmpty()
                && userRegistrationDTO.getSurname() != null && !userRegistrationDTO.getSurname().isEmpty()
                && userRegistrationDTO.getPassword() != null && !userRegistrationDTO.getPassword().isEmpty();
    }

    public boolean isUserAlreadyExists(UserRegistrationDTO userRegistrationDTO){
        return userRepository.findByEmail(userRegistrationDTO.getEmail()) != null;
    }

}
