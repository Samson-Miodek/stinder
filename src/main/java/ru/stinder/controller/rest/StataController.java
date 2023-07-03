package ru.stinder.controller.rest;

import ru.stinder.entity.Stata;
import ru.stinder.repository.StataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StataController {

    @Autowired
    private StataRepository stataRepository;

    @GetMapping("/admin/stata")
    List<Stata> all() {
        return stataRepository.findAll();
    }

}
