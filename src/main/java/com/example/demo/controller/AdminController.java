package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.entity.security.Authority;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashSet;
import java.util.Set;

@Controller
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/admin")
    public String index(Model model){

        model.addAttribute("users", userRepository.findAll());

        return "admindir/admin";
    }

    @GetMapping("/admin/addModerator/{id}")
    public String addModerator(@PathVariable long id){
        User user = userRepository.findById(id).get();
        Set<Authority> aut = new HashSet<>();
        aut.add(Authority.USER);
        aut.add(Authority.MODERATOR);
        if (user.getAuthorities().contains(Authority.ADMIN))
            aut.add(Authority.ADMIN);
        user.setAuthorities(aut);
        userRepository.save(user);
        return "redirect:/admin";
    }

    @GetMapping("/admin/removeModerator/{id}")
    public String removeModerator(@PathVariable long id){
        User user = userRepository.findById(id).get();
        Set<Authority> aut = new HashSet<>();
        aut.add(Authority.USER);
        if (user.getAuthorities().contains(Authority.ADMIN)){
            aut.add(Authority.MODERATOR);
            aut.add(Authority.ADMIN);
        }

        user.setAuthorities(aut);
        userRepository.save(user);
        return "redirect:/admin";
    }

}
