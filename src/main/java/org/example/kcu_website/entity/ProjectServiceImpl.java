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
import java.util.Optional;

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

    @Override
    public Optional<Project> findProjectById(Long projectId) {
        return projectRepository.findById(projectId);
    }

    @Override
    public Optional<Participant> findParticipantById(Long participantId) {
        return participantRepository.findById(participantId);
    }

    @Override
    public Optional<Semester> findSemesterById(Long semesterId) {
        return semesterRepository.findById(Long.valueOf(semesterId));
    }

    // saveOrUpdateParticipant 구현
    @Override
    public Participant saveOrUpdateParticipant(Participant participant) {
        return participantRepository.save(participant);
    }

    @Override
    public Project saveOrUpdateProject(Project project) {
        return projectRepository.save(project);
    }

    @Override
    public Semester saveOrUpdateSemester(Semester semester) {
        return semesterRepository.save(semester);
    }

    @Override
    public List<Project> getProjectsBySemesterId(Long semesterId) {
        return projectRepository.findBySemesterId(semesterId);
    }
}
