package ru.stinder.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRegistrationDTO {
    private String name;
    private String surname;
    private String email;
    private String password;
}
