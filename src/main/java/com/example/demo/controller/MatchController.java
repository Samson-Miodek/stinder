package com.example.demo.controller;

import com.example.demo.entity.Hobby;
import com.example.demo.entity.Stata;
import com.example.demo.entity.User;
import com.example.demo.repository.HobbyRepository;
import com.example.demo.repository.StataRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Controller
public class MatchController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HobbyRepository hobbyRepository;

    @Autowired
    private StataRepository stataRepository;

    //просто статистика посещения
    private static Stata lastStata = new Stata("1.1.1.1",new Timestamp(new Date().getTime()));
    //    Вывод всех пользователей и их хобби
    @GetMapping()
    public String index(Principal principal, Model model, HttpServletRequest request) {
        model.addAttribute("userList", userRepository.findAll());
        if (principal != null) {
            User user = userRepository.findByEmail(principal.getName());
            model.addAttribute("myId", user.getId());

            Set<Hobby> hobbies = user.getHobbies();
            model.addAttribute("myHobbis", hobbies);
        }

        Stata stata = new Stata(request.getRemoteAddr(),new Timestamp(new Date().getTime()));
        long deltaTime = stata.getDate().getTime()-lastStata.getDate().getTime();
        long sec = TimeUnit.MILLISECONDS.toSeconds(deltaTime);

        if(sec > 10){
            lastStata = stata;
            stataRepository.save(stata);
        }

        return "index";
    }


    //    Вывод всех пользователей, у которых есть определённое хобби
    @GetMapping("/find/{id}")
    public String find(@PathVariable long id, Principal principal, Model model) {
        Optional<Hobby> hobby = hobbyRepository.findById(id);
        List<User> users = new ArrayList<>();
        if (principal != null && hobby.isPresent()) {
            users = userRepository.findAllByHobbiesContains(hobby.get());
            User user = userRepository.findByEmail(principal.getName());
            model.addAttribute("myId", user.getId());

            Set<Hobby> hobbies = user.getHobbies();

            model.addAttribute("myHobbis", hobbies);
            model.addAttribute("HOBBY", hobby.get());
        } else {
            return "redirect:/";
        }
        model.addAttribute("userList", users);

        return "find";
    }


    //Вывод всех пользователей, у которых есть хотя одно моё хобби
    @GetMapping("/match")
    public String match(Principal principal, Model model) {
        User user = userRepository.findByEmail(principal.getName());
        List<User> userList = new ArrayList<>();
        Set<Hobby> hobbies = user.getHobbies();

        for (User u : userRepository.findAll()) {
            for (Hobby myHobby : hobbies) {
                if (u.getHobbies().contains(myHobby)) {
                    userList.add(u);
                    break;
                }
            }
        }

        model.addAttribute("userList", userList);

        if (true) {
            model.addAttribute("myHobbisDel", hobbies);
            model.addAttribute("myHobbis", hobbies);
            model.addAttribute("myId", user.getId());

        } else {
            return "redirect:/";
        }

        return "match";
    }


    //вывод всех по ролям
    @GetMapping("/match/{role}")
    public String matchByRole(@PathVariable String role, Principal principal, Model model) {
        if (role.equals("Студент") || role.equals("Студент-аспирант") || role.equals("Научный сотрудник") || role.equals("Научный руководитель"))
            model.addAttribute("userList", userRepository.findAllByRole(role));
        else
            return "redirect:/";

        if (principal != null) {
            User user = userRepository.findByEmail(principal.getName());
            Set<Hobby> hobbies = user.getHobbies();
            model.addAttribute("myHobbis", hobbies);
            model.addAttribute("myId", user.getId());

        } else {
            return "redirect:/";
        }
        model.addAttribute("ROLE", role);

        return "matchByRole";
    }

}
