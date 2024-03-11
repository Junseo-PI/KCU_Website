package org.example.kcu_website.entity;

import org.example.kcu_website.model.Participant;
import org.example.kcu_website.model.Project;
import org.example.kcu_website.model.Semester;
import org.example.kcu_website.model.User;

import java.util.List;
import java.util.Optional;

public interface ProjectService {
    List<Project> getAllProjects();
    List<Participant> getAllParticipants();
    List<Semester> getAllSemesters();
    List<User> getAllUsers();

    Optional<Project> findProjectById(Long projectId);
    Optional<Participant> findParticipantById(Long participantId);
    Optional<Semester> findSemesterById(Long semesterId);

    Participant saveOrUpdateParticipant(Participant participant);
    Semester saveOrUpdateSemester(Semester semester);
    Project saveOrUpdateProject(Project project);

    List<Project> getProjectsBySemesterId(Long semesterId);
}
