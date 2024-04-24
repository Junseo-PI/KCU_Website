package org.example.kcu_website.entity;

import org.example.kcu_website.model.*;
import org.example.kcu_website.repository.*;
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
    private final BannerRepository bannerRepository;
    private final LeaderRepository leaderRepository;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository, ParticipantRepository participantRepository,
                              SemesterRepository semesterRepository, UserRepository userRepository,
                              BannerRepository bannerRepository, LeaderRepository leaderRepository) {
        this.projectRepository = projectRepository;
        this.participantRepository = participantRepository;
        this.semesterRepository = semesterRepository;
        this.userRepository = userRepository;
        this.bannerRepository = bannerRepository;
        this.leaderRepository = leaderRepository;
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
    public List<Banner> getAllBanners() {
        return bannerRepository.findAll();
    }

    @Override
    public List<Leader> getAllLeaders() {
        return leaderRepository.findAll();
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

    @Override
    public Optional<Banner> findBannerById(Long bannerId) {
        return bannerRepository.findById(bannerId);
    }

    @Override
    public Optional<Leader> findLeaderById(Long leaderId) {
        return leaderRepository.findById(leaderId);
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
    public Banner saveOrUpdateBanner(Banner banner) {
        return bannerRepository.save(banner);
    }

    @Override
    public Leader saveOrUpdateLeader(Leader leader) {
        return leaderRepository.save(leader);
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
