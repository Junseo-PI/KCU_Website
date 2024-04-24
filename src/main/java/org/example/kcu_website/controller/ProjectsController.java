package org.example.kcu_website.controller;

import org.example.kcu_website.entity.ProjectServiceImpl;
import org.example.kcu_website.model.*;
import org.example.kcu_website.repository.ParticipantRepository;
import org.example.kcu_website.repository.ProjectRepository;
import org.example.kcu_website.repository.SemesterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class ProjectsController {
  @Autowired
  private ProjectRepository projectRepository;

  @Autowired
  private SemesterRepository semesterRepository;
  @Autowired
  private ParticipantRepository participantRepository;
  private ProjectServiceImpl projectService;

  public ProjectsController(ProjectRepository projectRepository, SemesterRepository semesterRepository, ParticipantRepository participantRepository, ProjectServiceImpl projectService) {
    this.projectRepository = projectRepository;
    this.semesterRepository = semesterRepository;
    this.participantRepository = participantRepository;
    this.projectService = projectService;
  }

  // 최신 학기로 리다이렉트
  @GetMapping("/projects")
  public String redirectToLatestSemester() {
    Optional<Semester> latestSemesterOpt = semesterRepository.findTopByOrderByNameDesc();
    return latestSemesterOpt.map(semester -> "redirect:/projects/" + semester.getName())
        .orElse("redirect:/errorPage");
  }

  // 특정 학기 프로젝트 전부 표시
  @GetMapping("/projects/{semesterName}")
  public String listProjectsBySemester(@PathVariable String semesterName, Model model) {
    Optional<Semester> semesterOpt = semesterRepository.findByName(semesterName);
    List<Semester> semesters = semesterRepository.findAll();

    return semesterOpt.map(semester -> {
      List<Project> projects = projectRepository.findBySemesterId(semester.getId());
      List<ProjectDTO> projectDTOs = projects.stream().map(project -> {
        ProjectDTO dto = new ProjectDTO();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setGithubLink(project.getGithubLink());
        dto.setSemesterId(project.getSemesterId());
        dto.setLevel(project.getLevel());
        dto.setLanguagesPlatforms(project.getLanguagesPlatforms());
        dto.setLongDescription(project.getLongDescription());
        dto.setShortDescription(project.getShortDescription());
        dto.setSemesterName(semester.getName());
        dto.setImages_link1(project.getImages_link1());

        List<Participant> participants = participantRepository.findByProjectId(project.getId());
        List<String> participantNames = participants.stream()
            .map(Participant :: getName)
            .collect(Collectors.toList());

        dto.setParticipantNames(participantNames);
        return dto;
      }).toList();

      semesters.sort(Comparator.comparingInt(s -> {
        int year = Integer.parseInt(s.getName().substring(2));
        int term = s.getName().startsWith("SP") ? 0 : 1;
        return year * 10 + term;
      }));

      model.addAttribute("projects", projects);
      model.addAttribute("semesterName", semester.getName());
      model.addAttribute("semesterId", semesterName);
      model.addAttribute("projectDTOs", projectDTOs);
      model.addAttribute("semesters", semesters);
      return "projects";
    }).orElse("redirect:/errorPage");
  }

  @GetMapping("/")
  public String index(Model model) {
    // 가장 최신 학기 가져오기
    Optional<Semester> latestSemesterOpt = semesterRepository.findTopByOrderByNameDesc();
    if (!latestSemesterOpt.isPresent()) {
      return "redirect:/errorPage"; // 오류 페이지로 리다이렉트
    }
    Semester latestSemester = latestSemesterOpt.get();

    // 최신 학기의 프로젝트들 가져오기
    List<Project> projects = projectRepository.findBySemesterId(latestSemester.getId());

    // 성능 최적화를 위해 한 번의 조회로 모든 필요한 학기 정보를 가져오기
    Map<Long, String> semesterNameMap = new HashMap<>();
    semesterRepository.findAll().forEach(s -> semesterNameMap.put(s.getId(), s.getName()));

    List<ProjectDTO> projectDTOs = projects.stream().map(project -> {
      ProjectDTO dto = new ProjectDTO();
      dto.setId(project.getId());
      dto.setName(project.getName());
      dto.setImages_link1(project.getImages_link1());

      List<Participant> participants = participantRepository.findByProjectId(project.getId());
      List<String> participantNames = participants.stream()
              .map(Participant::getName)
              .collect(Collectors.toList());

      dto.setParticipantNames(participantNames);
      dto.setLevel(project.getLevel());
      // 학기 ID를 사용하여 학기 이름을 설정
      dto.setSemesterName(semesterNameMap.get(project.getSemesterId()));

      return dto;
    }).collect(Collectors.toList());

    // 모델에 추가

    List<Banner> banners = projectService.getAllBanners();
    model.addAttribute("banners", banners);

    model.addAttribute("latestSemesterName", latestSemester.getName());
    model.addAttribute("projectDTOs", projectDTOs);

    return "index"; // index.html로 리턴
  }

}
