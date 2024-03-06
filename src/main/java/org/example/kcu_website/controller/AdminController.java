package org.example.kcu_website.controller;

import org.example.kcu_website.entity.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class AdminController {
    @Autowired
    private ProjectService projectService;

    @RequestMapping("/admin/{tableName}")
    public String adminTable(Model model, @PathVariable("tableName") String tableName) {
        List<?> items = null;
        switch (tableName.toLowerCase()) {
            case "projects":
                items = projectService.getAllProjects();
                break;

            case "participants":
                items = projectService.getAllParticipants();
                break;

            case "semesters":
                items = projectService.getAllSemesters();
                break;

            case "users":
                items = projectService.getAllUsers();
                break;
        }

        model.addAttribute("tableName", tableName.substring(0, 1).toUpperCase() + tableName.substring(1));
        model.addAttribute("items", items);
        return "adminDbView";
    }

    @RequestMapping("/admin/{tableName}/{itemId}/change")
    public String adminChangeItem(Model model, @PathVariable String tableName, @PathVariable Integer itemId) {

        return "adminAddChange";
    }

    @RequestMapping("/admin/{tableName}/add")
    public String adminAddItem(Model model, @PathVariable String tableName) {

        return "adminAddChange";
    }
}
