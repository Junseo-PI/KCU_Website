package org.example.kcu_website.entity;

import org.example.kcu_website.model.Participant;
import org.example.kcu_website.model.Project;
import org.example.kcu_website.model.Semester;
import org.example.kcu_website.model.User;
import org.example.kcu_website.repository.ParticipantRepository;
import org.example.kcu_website.repository.ProjectRepository;
import org.example.kcu_website.repository.SemesterRepository;
import org.example.kcu_website.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final ParticipantRepository participantRepository;
    private final SemesterRepository semesterRepository;
    private final UserRepository userRepository;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository, ParticipantRepository participantRepository,
                              SemesterRepository semesterRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.participantRepository = participantRepository;
        this.semesterRepository = semesterRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @Override
    public List<Participant> getAllParticipants() {
        return participantRepository.findAll();
    }

    @Override
    public List<Semester> getAllSemesters() {
        return semesterRepository.findAll();
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
