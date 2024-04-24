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
    List<Leader> getAllLeaders();
    List<GetInvolved> getAllGetInvolved();

    Optional<Project> findProjectById(Long projectId);
    Optional<Participant> findParticipantById(Long participantId);
    Optional<Semester> findSemesterById(Long semesterId);
    Optional<Banner> findBannerById(Long bannerId);
    Optional<Leader> findLeaderById(Long leaderId);

    Participant saveOrUpdateParticipant(Participant participant);
    Semester saveOrUpdateSemester(Semester semester);
    Project saveOrUpdateProject(Project project);
    Banner saveOrUpdateBanner(Banner banner);
    Leader saveOrUpdateLeader(Leader leader);
    GetInvolved saveOrUpdateGetInvolved(GetInvolved getInvolved);

    List<Project> getProjectsBySemesterId(Long semesterId);
}
