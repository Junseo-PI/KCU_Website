package org.example.kcu_website.controller;

import org.example.kcu_website.model.Participant;
import org.example.kcu_website.model.Project;
import org.example.kcu_website.model.ProjectDTO;
import org.example.kcu_website.model.Semester;
import org.example.kcu_website.repository.ParticipantRepository;
import org.example.kcu_website.repository.ProjectRepository;
import org.example.kcu_website.repository.SemesterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class ProjectsController {
  @Autowired
  private ProjectRepository projectRepository;

  @Autowired
  private SemesterRepository semesterRepository;
  @Autowired
  private ParticipantRepository participantRepository;

  // 최신 학기로 리다이렉트
  @GetMapping("/projects")
  public String redirectToLatestSemester() {
    Optional<Semester> latestSemesterOpt = semesterRepository.findTopByOrderBySemesterNameDesc();
    return latestSemesterOpt.map(semester -> "redirect:/projects/" + semester.getSemesterName())
        .orElse("redirect:/errorPage");
  }

  // 특정 학기 프로젝트 전부 표시
  @GetMapping("/projects/{semesterName}")
  public String listProjectsBySemester(@PathVariable String semesterName, Model model) {
    Optional<Semester> semesterOpt = semesterRepository.findBySemesterName(semesterName);
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
        dto.setSemesterName(semester.getSemesterName());
        dto.setImages_link1(project.getImages_link1());

        List<Participant> participants = participantRepository.findByProjectId(project.getId());
        List<String> participantNames = participants.stream()
            .map(Participant :: getName)
            .collect(Collectors.toList());

        dto.setParticipantNames(participantNames);
        return dto;
      }).toList();

      model.addAttribute("projects", projects);
      model.addAttribute("semesterName", semester.getSemesterName());
      model.addAttribute("semesterId", semesterName);
      model.addAttribute("projectDTOs", projectDTOs);
      model.addAttribute("semesters", semesters);
      return "projects";
    }).orElse("redirect:/errorPage");

  }
}
