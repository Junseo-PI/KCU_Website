package org.example.kcu_website.controller;

import org.example.kcu_website.entity.ProjectServiceImpl;
import org.example.kcu_website.model.Leader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
import java.util.List;

@Controller
public class LeaderController {
    @Autowired
    private ProjectServiceImpl projectService;

    @GetMapping("/leaders")
    public String showLeaders(Model model) {
        List<Leader> leaders = projectService.getAllLeaders();

        Collections.reverse(leaders);

        model.addAttribute("leaders", leaders);

        return "leaders";
    }
}
