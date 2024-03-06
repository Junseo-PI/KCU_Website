package org.example.kcu_website.entity;

import org.example.kcu_website.model.Participant;
import org.example.kcu_website.model.Project;
import org.example.kcu_website.model.Semester;
import org.example.kcu_website.model.User;

import java.util.List;

public interface ProjectService {
    List<Project> getAllProjects();
    List<Participant> getAllParticipants();
    List<Semester> getAllSemesters();
    List<User> getAllUsers();
}
