package org.example.kcu_website.entity;

import org.example.kcu_website.model.*;

import java.util.List;
import java.util.Optional;

public interface ProjectService {
    List<Project> getAllProjects();
    List<Participant> getAllParticipants();
    List<Semester> getAllSemesters();
    List<User> getAllUsers();
    List<Banner> getAllBanners();

    Optional<Project> findProjectById(Long projectId);
    Optional<Participant> findParticipantById(Long participantId);
    Optional<Semester> findSemesterById(Long semesterId);
    Optional<Banner> findBannerById(Long bannerId);

    Participant saveOrUpdateParticipant(Participant participant);
    Semester saveOrUpdateSemester(Semester semester);
    Project saveOrUpdateProject(Project project);
    Banner saveOrUpdateBanner(Banner banner);

    List<Project> getProjectsBySemesterId(Long semesterId);
}
