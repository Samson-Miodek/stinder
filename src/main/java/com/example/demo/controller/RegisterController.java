package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.entity.security.Authority;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.SecurityConfiguration;
import com.example.demo.security.service.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.UUID;

@Controller
public class RegisterController {
//reset-password
//reset-password
//reset-password
    @Value("${site.host}")
    private String HOST;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailSender mailSender;

    private static boolean debbug = true;

    @GetMapping("/register")
    public String getRegister(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String postRegister(@ModelAttribute User user) {
        if(user.getEmail() == null || user.getEmail().isEmpty())
            return "redirect:register";

        if (userRepository.findByEmail(user.getEmail()) != null)
            return "redirect:register?mailError";

//        user.setActive(true);
        user.setAuthorities(Collections.singleton(Authority.USER));
        user.setPassword(SecurityConfiguration.passwordEncoder().encode(user.getPassword()));
        user.setActivationCode(UUID.randomUUID().toString());
        user.setRole("Студент");

        String message = String.format("Здравствуйте, %s! Чтобы подтвердить вашу почту перейдите по ссылке %s/activate/%s", user.getName(), HOST, user.getActivationCode());

        if(debbug)
            user.setActive(true);
        userRepository.save(user);

        if (debbug)
            return "redirect:/login?emailSuccess";

        try {
            mailSender.send(user.getEmail(), "Activation code", message);
        }catch (Exception e){
            user.setActive(true);
            return "redirect:/login";
        }

        return "redirect:/login?emailNotActive";
    }

    @GetMapping("/activate/{code}")
    public String activateEmail(@PathVariable String code) {
        User user = userRepository.findByActivationCode(code);

        if (user == null)
            return "redirect:/login?emailSuccess";

        user.setActivationCode(null);
        user.setActive(true);

        userRepository.save(user);

        return "redirect:/login?emailSuccess";
    }


    @GetMapping("/reset")
    public String getResetForm() {
        return "reset";
    }

    @PostMapping("/reset")
    public String postResetForm(@RequestParam("email") String email) {
        email = email.toLowerCase();
        User user = userRepository.findByEmail(email);

        if(user == null)
            return "redirect:/reset?emailFailed";

        String code = UUID.randomUUID().toString();
        user.setActivationCode(code);
        userRepository.save(user);

        String message = String.format("Здравствуйте, %s! Чтобы сбросить пароль перейдите по ссылке %s/reset-password/%s", user.getName(), HOST, code);


        try{
            mailSender.send(email, "Сброс пароля", message);
        }catch (Exception e){
            return "redirect:/reset?error";
        }

        return "redirect:/reset?emailSuccess";
    }

    @GetMapping("/reset-password/{code}")
    public String getResetPasswordForm(@PathVariable String code , Model model){
        model.addAttribute("code",code);
        return "resetPassword";
    }

    @PostMapping("/reset-password/{code}")
    public String postResetPasswordForm(@RequestParam("code") String code, @RequestParam("password") String password) {
        User user = userRepository.findByActivationCode(code);
        if (user == null)
            return "redirect:/";

        user.setPassword(SecurityConfiguration.passwordEncoder().encode(password));
        user.setActivationCode(null);
        userRepository.save(user);

        return "redirect:/login?resetSuccess";
    }


    @GetMapping("/sendMail")
    public String getSendMail(){
        return "sendMail";
    }

    @PostMapping("/sendMail")
    public String postSendMail(@RequestParam("email") String email) {
        if(email == null || email.isEmpty())
            return "redirect:/sendMail?emailFailed";

        User user = userRepository.findByEmail(email);
        if (user == null)
            return "redirect:/sendMail?emailFailed";

        String message = String.format("Здравствуйте, %s! Чтобы подтвердить вашу почту перейдите по ссылке %s/activate/%s", user.getName(), HOST, user.getActivationCode());

        try {
            mailSender.send(user.getEmail(), "Activation code", message);
        }catch (Exception e){
            user.setActive(true);
            return "redirect:/login?emailSuccess";
        }

        return "redirect:/sendMail?emailSuccess";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
