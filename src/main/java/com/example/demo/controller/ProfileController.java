package com.example.demo.controller;

import com.example.demo.entity.Hobby;
import com.example.demo.entity.Publication;
import com.example.demo.entity.User;
import com.example.demo.repository.HobbyRepository;
import com.example.demo.repository.PublicationRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashSet;
import java.util.Optional;

@Controller
public class ProfileController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PublicationRepository publicationRepository;

    @Autowired
    private HobbyRepository hobbyRepository;

    @GetMapping("profile")
    public String profile(Principal principal, Model model){

        User user = userRepository.findByEmail(principal.getName());

        model.addAttribute("user",user);

        return "profile";
    }

    @PostMapping("updateProfile")
    public String updateProfile(@ModelAttribute User newUser,Principal principal){


        User user = userRepository.findByEmail(principal.getName());
        user.setName(newUser.getName());
        user.setSurname(newUser.getSurname());
        user.setPatronymic(newUser.getPatronymic());
        user.setBirthday(newUser.getBirthday());


        user.setSpecialty(newUser.getSpecialty());
        user.setAreaScientificInterests(newUser.getAreaScientificInterests());
        user.setAcademicTitle(newUser.getAcademicTitle());
        user.setAcademicDegree(newUser.getAcademicDegree());
        user.setPosition(newUser.getPosition());

        user.setRole(newUser.getRole());
        user.setAbout(newUser.getAbout());
        user.setStatus(newUser.getStatus());

        userRepository.save(user);
        return "redirect:/profile";
    }


    @GetMapping("profile/{id}")
    public String profileById(@PathVariable long id, Model model){

        Optional<User> user = userRepository.findById(id);

        if(!user.isPresent())
            return "redirect:/";

        model.addAttribute("user",user.get());

        return "profileById";
    }


    @GetMapping("/profile/addHobby")
    public String getAddHobby(Model model){

        model.addAttribute("hobbies",hobbyRepository.findAll());

        return "addHobby";
    }


    @PostMapping("/profile/addHobby")
    public String postAddHobby(@RequestParam("name") String hobbyName, Principal principal){
        User user = userRepository.findByEmail(principal.getName());

        Hobby hobby = hobbyRepository.findByName(hobbyName);


        if(user.getHobbies()==null)
            user.setHobbies(new HashSet<>());

        if(user.getHobbies().size() >= 6 )
            return "redirect:/profile";

        if(hobby==null)
            hobby = new Hobby(hobbyName);

        hobbyRepository.save(hobby);

        user.getHobbies().add(hobby);
        userRepository.save(user);
        System.out.println(hobbyName);

        return "redirect:/profile";
    }

    @GetMapping("/profile/deleteHobby/{id}")
    public String deleteHobby(@PathVariable long id,Principal principal){
        User user = userRepository.findByEmail(principal.getName());

        Hobby hobby = hobbyRepository.findById(id).get();

        if(user.getHobbies()==null || hobby==null)
            return "redirect:/profile";


        user.getHobbies().remove(hobby);
        userRepository.save(user);

        return "redirect:/profile";
    }


    @GetMapping("/profile/publications")
    public String getPublications(Principal principal,Model model){
        User user = userRepository.findByEmail(principal.getName());

        model.addAttribute("publications",user.getPublications());

        return "publicationsDir/publications";
    }


    @PostMapping("/profile/publications/add")
    public String addPublications(@RequestParam("name") String name, @RequestParam("link") String link, Principal principal){

        if(name.length()<10 || link.length() < 10)
            return "redirect:/profile/publications";

        User user = userRepository.findByEmail(principal.getName());

        if(user.getPublications().size()<100){
            Publication publication = new Publication(name,link);
            user.getPublications().add(publication);
            publicationRepository.save(publication);
            userRepository.save(user);
        }

        return "redirect:/profile/publications";
    }

    @GetMapping("profile/{id}/publications")
    public String getPublicationsByUserId(@PathVariable long id, Model model){

        Optional<User> user = userRepository.findById(id);

        if(!user.isPresent())
            return "redirect:/";

        model.addAttribute("publications",user.get().getPublications());

        return "publicationsDir/publicationsById";
    }

    @GetMapping("profile/publications/delete/{id}")
    public String deletePublicationsById(@PathVariable long id, Principal principal){
        User user = userRepository.findByEmail(principal.getName());

        for (Publication p : user.getPublications()) {
            if(p.getId() == id)
            {
                user.getPublications().remove(p);
                publicationRepository.delete(p);
                break;
            }
        }
        userRepository.save(user);

        return "redirect:/profile/publications";
    }

}
