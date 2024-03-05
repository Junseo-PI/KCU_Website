package org.example.kcu_website.controller;

import org.example.kcu_website.model.Participant;
import org.example.kcu_website.model.Project;
import org.example.kcu_website.model.ProjectDTO;
import org.example.kcu_website.repository.ParticipantRepository;
import org.example.kcu_website.repository.ProjectRepository;
import org.example.kcu_website.repository.SemesterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class ProjectDetailController {
  @Autowired
  private ProjectRepository projectRepository;
  @Autowired
  private ParticipantRepository participantRepository;

  @GetMapping("/projects/{semesterName}/{projectId}")
  public String projectDetails(@PathVariable String semesterName, @PathVariable Long projectId,
      Model model) {
    Optional<Project> projectOpt = projectRepository.findById(projectId);

    return projectOpt.map(project -> {
      ProjectDTO dto = new ProjectDTO();
      dto.setId(project.getId());
      dto.setName(project.getName());
      dto.setLevel(project.getLevel());
      dto.setLanguagesPlatforms(project.getLanguagesPlatforms());
      dto.setShortDescription(project.getShortDescription());
      dto.setLongDescription(project.getLongDescription());
      dto.setSemesterId(project.getSemesterId());
      dto.setSemesterName(semesterName);

      if (!project.getGithubLink().contains("http")) {
        dto.setGithubLink("https://" + project.getGithubLink());
      } else {
        dto.setGithubLink(project.getGithubLink());
      }

      List<Participant> participants = participantRepository.findByProjectId(project.getId());
      dto.setParticipants(participants);

      model.addAttribute("projectDTO", dto);
      return "projectDetail";
    }).orElse("redirect:/errorPage");
  }
}
