package com.example.demo.controller.rest;

import com.example.demo.entity.Stata;
import com.example.demo.repository.StataRepository;
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
