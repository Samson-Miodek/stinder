package ru.stinder.controller;

import ru.stinder.entity.User;
import ru.stinder.entity.security.Authority;
import ru.stinder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ModeratorController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/moderator")
    public String index(Model model){
        model.addAttribute("users",userRepository.findAll() );
        return "moderatorDir/moderator";
    }


    @GetMapping("/moderator/ban/{id}")
    public String ban(@PathVariable long id){
        User user = userRepository.findById(id).get();
        if (user.getAuthorities().contains(Authority.ADMIN) || user.getAuthorities().contains(Authority.MODERATOR)) {
            return "redirect:/";
        }
        user.setActive(false);
        userRepository.save(user);
        return "redirect:/moderator";
    }

    @GetMapping("/moderator/unban/{id}")
    public String unban(@PathVariable long id){
        User user = userRepository.findById(id).get();
        user.setActive(true);
        userRepository.save(user);
        return "redirect:/moderator";
    }
}
