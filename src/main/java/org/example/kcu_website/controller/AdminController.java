package org.example.kcu_website.controller;

import org.example.kcu_website.entity.ProjectService;
import org.example.kcu_website.model.*;
import org.example.kcu_website.repository.SemesterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class AdminController {
    private final ProjectService projectService;

    @Autowired
    private SemesterRepository semesterRepository;

    public AdminController(ProjectService projectService, SemesterRepository semesterRepository) {
        this.projectService = projectService;
        this.semesterRepository = semesterRepository;
    }

    @RequestMapping("/admin/{tableName}")
    public String adminTable(Model model, @PathVariable("tableName") String tableName) {
        List<?> items = null;
        switch (tableName.toLowerCase()) {
            case "projects":
                items = projectService.getAllProjects();
                break;

            case "participants":
                items = projectService.getAllParticipants();
                break;

            case "semesters":
                items = projectService.getAllSemesters();
                items = ((List<Semester>) items).stream()
                        .sorted(Comparator.comparingInt(s -> {
                            int year = Integer.parseInt(s.getName().substring(2));
                            int term = s.getName().startsWith("SP") ? 0 : 1; // SP는 0, FA는 1로 처리하여 봄 학기가 가을 학기보다 먼저 오도록 함
                            return (year * 10) + term; // 연도와 학기를 조합한 숫자를 기준으로 정렬
                        }))
                        .collect(Collectors.toList());
                break;

            case "users":
                items = projectService.getAllUsers();
                break;
        }

        model.addAttribute("tableName", tableName.substring(0, 1).toUpperCase() + tableName.substring(1));
        model.addAttribute("items", items);
        return "adminDbView";
    }

    @GetMapping("/admin/participants/{participantId}/change")
    public String showParticipantEditForm(@PathVariable Long participantId, Model model) {
        Optional<Participant> participantOpt = projectService.findParticipantById(participantId);
        if (!participantOpt.isPresent()) {
            return "redirect:/admin/participants";
        }

        Participant participant = participantOpt.get();
        model.addAttribute("entity", participant);

        List<Semester> semesters = projectService.getAllSemesters();
        model.addAttribute("semesters", semesters);

        Optional<Project> projectOpt = projectService.findProjectById(participant.getProjectId());
        if (projectOpt.isPresent()) {
            Project project = projectOpt.get();
            model.addAttribute("selectedSemesterId", project.getSemesterId());
            List<Project> projectsInSemester = projectService.getProjectsBySemesterId(project.getSemesterId());
            model.addAttribute("projectsInSemester", projectsInSemester);
        }
        model.addAttribute("tableName", "Participants");

        return "adminChange";
    }

    @PostMapping("/admin/participants/{participantId}/change")
    public String updateParticipant(@PathVariable Long participantId,
                                    @ModelAttribute Participant participant,
                                    RedirectAttributes redirectAttributes) {
        if (participant.getId() == null) {
            participant.setId(participantId);
        }

        projectService.saveOrUpdateParticipant(participant);
        redirectAttributes.addFlashAttribute("success", "Participant updated successfully!");
        return "redirect:/admin/participants";
    }

    @GetMapping("/admin/projects/{projectId}/change")
    public String showProjectEditForm(@PathVariable Long projectId, Model model) {
        Optional<Project> projectOpt = projectService.findProjectById(projectId);

        if (!projectOpt.isPresent()) {
            return "redirect:/admin/projects";
        }

        projectOpt.get().setLongDescription(projectOpt.get().getLongDescription().replace("<br>", "\n"));

        model.addAttribute("entity", projectOpt.get());

        List<Semester> semesters = projectService.getAllSemesters();
        model.addAttribute("semesters", semesters);
        model.addAttribute("tableName", "Projects");
        model.addAttribute("selectedSemesterId", projectOpt.get().getSemesterId());

        return "adminProjectChange";
    }

    @PostMapping("/admin/projects/{projectId}/change")
    public String updateProject(@PathVariable Long projectId,
                                @ModelAttribute("projectImageDTO") ProjectImageDTO projectImageDTO,
                                RedirectAttributes redirectAttributes) {
        try {
            // 이미지 파일 처리
            MultipartFile image1 = projectImageDTO.getImages_link1();
            MultipartFile image2 = projectImageDTO.getImages_link2();
            MultipartFile image3 = projectImageDTO.getImages_link3();

            String imagesLink1 = null, imagesLink2 = null, imagesLink3 = null;

            if (image1 != null && !image1.isEmpty()) {
                imagesLink1 = saveImage(image1, projectId, "1");
            }
            if (image2 != null && !image2.isEmpty()) {
                imagesLink2 = saveImage(image2, projectId, "2");
            }
            if (image3 != null && !image3.isEmpty()) {
                imagesLink3 = saveImage(image3, projectId, "3");
            }

            // 프로젝트 엔티티 업데이트 로직
            Project project = projectService.findProjectById(projectId).orElseThrow(() -> new Exception("Project not found"));

            // DTO를 이용하여 프로젝트 업데이트
            updateProjectEntityFromDTO(project, projectImageDTO, imagesLink1, imagesLink2, imagesLink3);

            projectService.saveOrUpdateProject(project);

            redirectAttributes.addFlashAttribute("success", "Project updated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error updating project: " + e.getMessage());
        }

        return "redirect:/admin/projects";
    }

    @GetMapping("/admin/semesters/{semesterId}/change")
    public String showSemesterEditForm(@PathVariable Long semesterId, Model model) {
        Semester semester = semesterRepository.findById(semesterId)
                .orElseThrow(() -> new IllegalStateException("Semester Not Found"));

        String term = semester.getName().substring(0, 2);
        String year = semester.getName().substring(2);

        model.addAttribute("tableName", "Semesters");
        model.addAttribute("term", term);
        model.addAttribute("year", year);
        model.addAttribute("entity", semester);

        return "adminChange";
    }

    @PostMapping("/admin/semesters/{semesterId}/change")
    public String updateSemester(@PathVariable Long semesterId, @RequestParam("term") String term, @RequestParam("year") String year, RedirectAttributes redirectAttributes) {
        String semesterName = term + year;

        Semester semester = semesterRepository.findById(semesterId)
                .orElseThrow(() -> new IllegalStateException("Semester Not Found"));

        semester.setName(semesterName);

        if(semester.getId() == null) {
            semester.setId(semesterId);
        }

        projectService.saveOrUpdateSemester(semester);

        redirectAttributes.addFlashAttribute("success", "Semester updated successfully!");
        return "redirect:/admin/semesters";
    }

    private String saveImage(MultipartFile file, Long projectId, String imageNumber) throws IOException {
        // TODO: 개발 환경에서는 /src/main/resources/ 아래에 파일을 프로그래매틱하게 저장하지만, 실제로 웹이 패키징되어 배포될 떄
        // 이 경로를 읽기 전용이 되며, 파일을 쓸 수 없게 되므로 애플리케이션 외부의 경로나 데이터베이스, 클라우드 스토리지 같은 영구 저장소를 사용하도록
        // 설정해야함.
        // 현재는 개발단계기 때문에 정적 리소스에 저장함.

        if (file.isEmpty()) {
            throw new IOException("Failed to save empty file for project ID: " + projectId);
        }

        Optional<Project> projectOpt = projectService.findProjectById(projectId);
        if (!projectOpt.isPresent()) {
            throw new IllegalStateException("Project Not Found");
        }

        Long semesterid = projectOpt.get().getSemesterId();
        Optional<Semester> semesterOpt = projectService.findSemesterById(semesterid);
        if (!semesterOpt.isPresent()) {
            throw new IllegalStateException("Semester Not Found");
        }

        String semesterName = semesterOpt.get().getName();

        String absolutePath = new File("").getAbsolutePath() + File.separator;
        String path = absolutePath + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "static" + File.separator + "images" + File.separator + semesterName;

        File filePath = new File(path);

        String newFileName = "";

        if (!filePath.exists()) {
            filePath.mkdirs();
        }

        if (!file.isEmpty()) {
            String contentType = file.getContentType();
            String originalFileExtension = "";

            if (!ObjectUtils.isEmpty(contentType)) {
                if (contentType.contains("image/jpeg")) {
                    originalFileExtension = ".jpg";
                } else if (contentType.contains("image/png")) {
                    originalFileExtension = ".png";
                } else {
                    throw new IOException("Only jpeg and png are allowed.");
                }
            }

            newFileName = projectId + "-" + imageNumber + originalFileExtension;
            String storedFilePath = filePath + "/" + newFileName;

            filePath = new File(path + "/" + newFileName);
            file.transferTo(filePath);
        }

        return "/images/" + semesterName + "/" + newFileName;
    }

    @PostMapping("/admin/projects/deleteImage/{projectId}/{imageField}")
    public ResponseEntity<?> deleteProjectImage(@PathVariable Long projectId, @PathVariable String imageField) {
        try {
            Project project = projectService.findProjectById(projectId)
                    .orElseThrow(() -> new Exception("Project not found"));

            // imageField 값에 따라 해당 이미지 경로를 DB에서 null로 설정
            switch (imageField) {
                case "images_link1":
                    project.setImages_link1(null);
                    break;
                case "images_link2":
                    project.setImages_link2(null);
                    break;
                case "images_link3":
                    project.setImages_link3(null);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid image field: " + imageField);
            }

            projectService.saveOrUpdateProject(project);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    private void updateProjectEntityFromDTO(Project project, ProjectImageDTO dto, String imagesLink1, String imagesLink2, String imagesLink3) {
        // DTO에서 기본 정보를 업데이트
        project.setName(dto.getName());

        // 이미지 링크 설정
        if (imagesLink1 != null) project.setImages_link1(imagesLink1);
        if (imagesLink2 != null) project.setImages_link2(imagesLink2);
        if (imagesLink3 != null) project.setImages_link3(imagesLink3);

        String htmlReadyText = dto.getLongDescription().replace("\n", "<br>");
        project.setGithubLink(dto.getGithubLink());
        project.setLongDescription(htmlReadyText);
        project.setShortDescription(dto.getShortDescription());
        project.setLevel(dto.getLevel());
        project.setName(dto.getName());
        project.setLanguagesPlatforms(dto.getLanguagesPlatforms());
        project.setSemesterId(dto.getSemesterId());
    }

    @GetMapping("/admin/semesters/add")
    public String showSemesterAddForm(Model model) {
        model.addAttribute("tableName", "Semesters");

        return "adminAdd";
    }

    @PostMapping("/admin/semesters/add")
    public String addSemester(@RequestParam("term") String term,
                                 @RequestParam("year") String year,
                                 RedirectAttributes redirectAttributes) {

        String semesterName = term + year;

        Optional<Semester> existingSemester = semesterRepository.findByName(semesterName);

        if (existingSemester.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Semester with name " + semesterName + " already exists!");
            return "redirect:/admin/semesters";
        }

        Semester newSemester = new Semester();

        newSemester.setName(semesterName);

        projectService.saveOrUpdateSemester(newSemester);

        redirectAttributes.addFlashAttribute("success", "Semester added successfully!");
        return "redirect:/admin/semesters";
    }

    @GetMapping("/admin/participants/add")
    public String showParticipantsAddForm(Model model) {
        List<Semester> semesters = projectService.getAllSemesters();
        model.addAttribute("semesters", semesters);

        model.addAttribute("tableName", "Participants");

        return "adminAdd";
    }

    @GetMapping("/admin/projects/bySemester/{semesterId}")
    @ResponseBody
    public ResponseEntity<List<Project>> getProjectsBySemester(@PathVariable Long semesterId) {
        List<Project> projects = projectService.getProjectsBySemesterId(semesterId);
        return ResponseEntity.ok(projects);
    }

    @PostMapping("/admin/participants/add")
    public String addParticipant( @RequestParam("name") String name,
                                    @RequestParam("role") String role,
                                    @RequestParam("email") String email,
                                    @RequestParam("projectId") String projectId,
                                    RedirectAttributes redirectAttributes) {

        Participant newParticipant = new Participant();

        newParticipant.setName(name);
        newParticipant.setRole(role);
        newParticipant.setEmail(email);
        newParticipant.setProjectId(Long.valueOf(projectId));

        projectService.saveOrUpdateParticipant(newParticipant);

        redirectAttributes.addFlashAttribute("success", "Participant updated successfully!");
        return "redirect:/admin/participants";
    }

    @GetMapping("/admin/projects/add")
    public String showProjectsAddForm(Model model) {
        model.addAttribute("tableName", "Projects");

        List<Semester> semesters = projectService.getAllSemesters();
        model.addAttribute("semesters", semesters);

        return "adminAddProjects";
    }

    @PostMapping("/admin/projects/add")
    public String addProject(@RequestParam("semesterId") String semesterId,
                             @RequestParam("projectName") String projectName,
                             @RequestParam("level") String level,
                             @RequestParam("languagesPlatforms") String languagesPlatforms,
                             @RequestParam("shortDescription") String shortDescription,
                             @RequestParam("longDescription") String longDescription,
                             @RequestParam("githubLink") String githubLink,
                             @ModelAttribute("projectImageDTO") ProjectImageDTO projectImageDTO,
                                RedirectAttributes redirectAttributes) {

        Project newProject = new Project();

        newProject.setSemesterId(Long.valueOf(semesterId));
        newProject.setName(projectName);
        newProject.setLevel(level);
        newProject.setLanguagesPlatforms(languagesPlatforms);
        newProject.setShortDescription(shortDescription);
        newProject.setLongDescription(longDescription);
        newProject.setGithubLink(githubLink);

        projectService.saveOrUpdateProject(newProject);

        Long projectId = newProject.getId();

        try {
            projectImageDTO.setName(newProject.getName());
            MultipartFile image1 = projectImageDTO.getImages_link1();
            MultipartFile image2 = projectImageDTO.getImages_link2();
            MultipartFile image3 = projectImageDTO.getImages_link3();

            String imagesLink1 = null, imagesLink2 = null, imagesLink3 = null;

            if (image1 != null && !image1.isEmpty()) {
                imagesLink1 = saveImage(image1, projectId, "1");
            }
            if (image2 != null && !image2.isEmpty()) {
                imagesLink2 = saveImage(image2, projectId, "2");
            }
            if (image3 != null && !image3.isEmpty()) {
                imagesLink3 = saveImage(image3, projectId, "3");
            }

            // DTO를 이용하여 프로젝트 업데이트
            updateProjectEntityFromDTO(newProject, projectImageDTO, imagesLink1, imagesLink2, imagesLink3);

            projectService.saveOrUpdateProject(newProject);

            redirectAttributes.addFlashAttribute("success", "Project updated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error updating project: " + e.getMessage());
        }

        redirectAttributes.addFlashAttribute("success", "Project added successfully!");
        return "redirect:/admin/projects";
    }
}
