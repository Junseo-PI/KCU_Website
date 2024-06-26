package org.example.kcu_website.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Random;
import org.example.kcu_website.entity.ProjectService;
import org.example.kcu_website.model.*;
import org.example.kcu_website.repository.GetInvolvedRepository;
import org.example.kcu_website.repository.SemesterRepository;
import org.example.kcu_website.repository.UserRepository;
import org.example.kcu_website.s3.S3UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GetInvolvedRepository getInvolvedRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private S3UploadService s3UploadService;

    public AdminController(ProjectService projectService, SemesterRepository semesterRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, S3UploadService s3UploadService, GetInvolvedRepository getInvolvedRepository) {
        this.projectService = projectService;
        this.semesterRepository = semesterRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.s3UploadService = s3UploadService;
        this.getInvolvedRepository = getInvolvedRepository;
    }

    @RequestMapping("/admin/{tableName}")
    public String adminTable(Model model, @PathVariable("tableName") String tableName, Principal principal) {
        String username = principal != null ? principal.getName() : "Anonymous";
        model.addAttribute("username", username);

        User user = userRepository.findByName(principal.getName());
        String authority = user.getAuthority();
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
                if (!authority.equals("ADMIN")) {
                    return "redirect:/errorPage";
                }
                items = projectService.getAllUsers();
                break;

            case "banners":
                if (!authority.equals("ADMIN")) {
                    return "redirect:/errorPage";
                }
                items = projectService.getAllBanners();
                break;

            case "leaders":
                if (!authority.equals("ADMIN")) {
                    return "redirect:/errorPage";
                }
                items = projectService.getAllLeaders();
                break;

            case "getinvolved":
                if (!authority.equals("ADMIN")) {
                    return "redirect:/errorPage";
                }
                items = projectService.getAllGetInvolved();
                break;
        }

        model.addAttribute("tableName", tableName.substring(0, 1).toUpperCase() + tableName.substring(1));
        model.addAttribute("items", items);
        return "adminDbView";
    }

    @GetMapping("/admin/participants/{participantId}/change")
    public String showParticipantEditForm(@PathVariable Long participantId, Model model, Principal principal) {
        String username = principal != null ? principal.getName() : "Anonymous";
        model.addAttribute("username", username);

        User user = userRepository.findByName(principal.getName());
        String authority = user.getAuthority();
        if (!authority.equals("ADMIN")) {
            return "redirect:/errorPage";
        }

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
    public String showProjectEditForm(@PathVariable Long projectId, Model model, Principal principal) {
        String username = principal != null ? principal.getName() : "Anonymous";
        model.addAttribute("username", username);

        User user = userRepository.findByName(principal.getName());
        String authority = user.getAuthority();
        if (!authority.equals("ADMIN")) {
            return "redirect:/errorPage";
        }

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
    public String showSemesterEditForm(@PathVariable Long semesterId, Model model, Principal principal) {
        String username = principal != null ? principal.getName() : "Anonymous";
        model.addAttribute("username", username);

        User user = userRepository.findByName(principal.getName());
        String authority = user.getAuthority();
        if (!authority.equals("ADMIN")) {
            return "redirect:/errorPage";
        }

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

        String newFileName = "";

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
        }

        return s3UploadService.saveFile(file, newFileName);
    }

    private String saveImage(MultipartFile file, String choice, Long id) throws IOException {
        String newFileName = "";
        if (choice.equals("Banner")) {
            if (file.isEmpty()) {
                throw new IOException("Failed to save empty file for banner ID: " + id);
            }

            Optional<Banner> bannerOpt = projectService.findBannerById(id);
            if (!bannerOpt.isPresent()) {
                throw new IllegalStateException("Banner Not Found");
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

                newFileName = "banner-" + id + originalFileExtension;
            }
        } else if (choice.equals("Leader")) {
            if (file.isEmpty()) {
                throw new IOException("Failed to save empty file for leader ID: " + id);
            }

            Optional<Leader> leaderOpt = projectService.findLeaderById(id);
            if (!leaderOpt.isPresent()) {
                throw new IllegalStateException("Leader Not Found");
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

                newFileName = "leader-" + id + originalFileExtension;
            }
        }

        return s3UploadService.saveFile(file, newFileName);
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
    public String showSemesterAddForm(Model model, Principal principal) {
        String username = principal != null ? principal.getName() : "Anonymous";
        model.addAttribute("username", username);

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
    public String showParticipantsAddForm(Model model, Principal principal) {
        String username = principal != null ? principal.getName() : "Anonymous";
        model.addAttribute("username", username);

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
    public String showProjectsAddForm(Model model, Principal principal) {
        String username = principal != null ? principal.getName() : "Anonymous";
        model.addAttribute("username", username);

        model.addAttribute("tableName", "Projects");

        List<Semester> semesters = projectService.getAllSemesters();
        model.addAttribute("semesters", semesters);

        return "adminAddProjectsLeaders";
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

    @GetMapping("/admin/users/add")
    public String showAdminAddUsersForm(Model model, Principal principal) {
        String username = principal != null ? principal.getName() : "Anonymous";
        model.addAttribute("username", username);

        // OTP ID 만들기
        String semester;
        String year;

        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();

        if (currentMonth >= 7 && currentMonth <= 11) {
            semester = "fa"; // 가을 학기
        } else {
            semester = "sp"; // 봄 학기
        }

        int currentYear = currentDate.getYear() % 100; // 년도의 마지막 2자리만 사용
        year = String.format("%02d", currentYear);

        User latestUser = this.userRepository.findFirstByOrderByIdDesc();
        String id = semester + year + String.format("%02d", latestUser.getId() + 1);

        // OTP 비밀번호 만들기 (6글자 랜덤)
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            int digit = random.nextInt(10); // 0부터 9까지의 숫자 중 랜덤하게 선택
            stringBuilder.append(digit);
        }

        String randomNumber = stringBuilder.toString();

        model.addAttribute("id", id);
        model.addAttribute("password", randomNumber);
        model.addAttribute("tableName", "Users");
        return "adminAddUsers";
    }

    @PostMapping("/admin/users/add")
    public String addUser(@RequestParam("userId") String userId,
                             @RequestParam("userPw") String userPw,
                             RedirectAttributes redirectAttributes) {

        User user = new User();
        user.setName(userId);
        user.setPassword(passwordEncoder.encode(userPw));
        user.setAuthority("USER");
        userRepository.save(user);

        redirectAttributes.addFlashAttribute("success", "User added successfully!");
        return "redirect:/admin/users";
    }

    @GetMapping("/admin")
    public String adminView(Model model, Principal principal) {
        String username = principal != null ? principal.getName() : "Anonymous";
        model.addAttribute("username", username);

        return "admin";
    }

    @GetMapping("/admin/banners/{bannerId}/change")
    public String showBannerEditForm(@PathVariable Long bannerId, Model model, Principal principal) {
        String username = principal != null ? principal.getName() : "Anonymous";
        model.addAttribute("username", username);

        User user = userRepository.findByName(principal.getName());
        String authority = user.getAuthority();
        if (!authority.equals("ADMIN")) {
            return "redirect:/errorPage";
        }

        Optional<Banner> bannerOpt = projectService.findBannerById(bannerId);

        if (!bannerOpt.isPresent()) {
            return "redirect:/admin/banners";
        }

        model.addAttribute("entity", bannerOpt.get());
        model.addAttribute("tableName", "Banners");

        return "adminChangeBannerLeader";
    }

    @PostMapping("/admin/banners/{bannerId}/change")
    public String updateBanner(@PathVariable Long bannerId,
                               @ModelAttribute("bannerImageDTO") BannerImageDTO bannerImageDTO,
                               RedirectAttributes redirectAttributes) {

        try {
            MultipartFile image = bannerImageDTO.getImageLink();

            String imageLink = null;

            if (image != null && !image.isEmpty()) {
                imageLink = saveImage(image, "Banner", bannerId);
            }

            Banner banner = projectService.findBannerById(bannerId).orElseThrow(() -> new Exception("Banner not found."));

            updateBannerEntityFromDTO(banner, bannerImageDTO, imageLink);

            projectService.saveOrUpdateBanner(banner);

            redirectAttributes.addFlashAttribute("success", "Banner updated successfully.");
        } catch (Exception e) {
            e.printStackTrace();;
            redirectAttributes.addFlashAttribute("error", "Error updating banner: " + e.getMessage());
        }

        return "redirect:/admin/banners";
    }

    private void updateBannerEntityFromDTO(Banner banner, BannerImageDTO dto, String imageLink) {
        // DTO에서 기본 정보를 업데이트
        banner.setName(dto.getName());

        // 이미지 링크 설정
        if (imageLink != null) banner.setImageLink(imageLink);

        banner.setName(dto.getName());
    }

    @GetMapping("/admin/leaders/{leaderId}/change")
    public String showLeaderEditForm(@PathVariable Long leaderId, Model model, Principal principal) {
        String username = principal != null ? principal.getName() : "Anonymous";
        model.addAttribute("username", username);

        User user = userRepository.findByName(principal.getName());
        String authority = user.getAuthority();
        if (!authority.equals("ADMIN")) {
            return "redirect:/errorPage";
        }

        Optional<Leader> leaderOpt = projectService.findLeaderById(leaderId);

        if (!leaderOpt.isPresent()) {
            return "redirect:/admin/leaders";
        }

        model.addAttribute("entity", leaderOpt.get());
        model.addAttribute("tableName", "Leaders");

        return "adminChangeBannerLeader";
    }

    @PostMapping("/admin/leaders/{leaderId}/change")
    public String updateLeader(@PathVariable Long leaderId,
                               @ModelAttribute("leaderImageDTO") LeaderImageDTO leaderImageDTO,
                               RedirectAttributes redirectAttributes) {

        try {
            MultipartFile image = leaderImageDTO.getImageLink();

            String imageLink = null;

            if (image != null && !image.isEmpty()) {
                imageLink = saveImage(image, "Leader", leaderId);
            }

            Leader leader = projectService.findLeaderById(leaderId).orElseThrow(() -> new Exception("Banner not found."));

            updateLeaderEntityFromDTO(leader, leaderImageDTO, imageLink);

            projectService.saveOrUpdateLeader(leader);

            redirectAttributes.addFlashAttribute("success", "Leader updated successfully.");
        } catch (Exception e) {
            e.printStackTrace();;
            redirectAttributes.addFlashAttribute("error", "Error updating leader: " + e.getMessage());
        }

        return "redirect:/admin/leaders";
    }

    private void updateLeaderEntityFromDTO(Leader leader, LeaderImageDTO dto, String imageLink) {
        // DTO에서 기본 정보를 업데이트
        leader.setName(dto.getName());

        // 이미지 링크 설정
        if (imageLink != null) leader.setImageLink(imageLink);

        leader.setEmail(dto.getEmail());
        leader.setEndSemester(dto.getEndSemester());
        leader.setIntroduction(dto.getIntroduction());
        leader.setStartSemester(dto.getStartSemester());
    }

    @GetMapping("/admin/leaders/add")
    public String showLeadersAddForm(Model model, Principal principal) {
        String username = principal != null ? principal.getName() : "Anonymous";
        model.addAttribute("username", username);

        model.addAttribute("tableName", "Leaders");

        return "adminAddProjectsLeaders";
    }

    @PostMapping("/admin/leaders/add")
    public String addLeaders(@RequestParam("leaderName") String leaderName,
                             @RequestParam("email") String email,
                             @RequestParam("startSemester") String startSemester,
                             @RequestParam("endSemester") String endSemester,
                             @RequestParam("introduction") String introduction,
                             @ModelAttribute("leaderImageDTO") LeaderImageDTO leaderImageDTO,
                             RedirectAttributes redirectAttributes) {

        Leader newLeader = new Leader();

        newLeader.setName(leaderName);
        newLeader.setEmail(email);
        newLeader.setStartSemester(startSemester);
        newLeader.setEndSemester(endSemester);
        newLeader.setIntroduction(introduction);

        projectService.saveOrUpdateLeader(newLeader);

        Long leaderId = newLeader.getId();

        try {
            leaderImageDTO.setName(newLeader.getName());
            MultipartFile image = leaderImageDTO.getImageLink();

            String imageLink = null;

            if (image != null && !image.isEmpty()) {
                imageLink = saveImage(image, "Leader", leaderId);
            }

            updateLeaderEntityFromDTO(newLeader, leaderImageDTO, imageLink);

            projectService.saveOrUpdateLeader(newLeader);

            redirectAttributes.addFlashAttribute("success", "Leader updated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error updating project: " + e.getMessage());
        }

        redirectAttributes.addFlashAttribute("success", "Project added successfully!");
        return "redirect:/admin/leaders";
    }

    @GetMapping("/admin/getinvolved/{involvedId}/change")
    public String showGetInvolvedEditForm(@PathVariable Long involvedId, Model model, Principal principal) {
        String username = principal != null ? principal.getName() : "Anonymous";
        model.addAttribute("username", username);

        GetInvolved getInvolved = getInvolvedRepository.findById(involvedId)
                .orElseThrow(() -> new IllegalStateException("Semester Not Found"));

        model.addAttribute("tableName", "GetInvolved");
        model.addAttribute("entity", getInvolved);

        return "adminChange";
    }

    @PostMapping("/admin/getinvolved/{involvedId}/change")
    public String updateGetInvolved(@PathVariable Long involvedId,
                                    @RequestParam("involvedName") String name,
                                    @RequestParam("startDate") String startDate,
                                    @RequestParam("endDate") String endDate,
                                    @RequestParam("formLink") String link,
                                    RedirectAttributes redirectAttributes) {
        GetInvolved getInvolved = getInvolvedRepository.findById(involvedId)
                .orElseThrow(() -> new IllegalStateException("GetInvolved Semester Not Found"));

        getInvolved.setName(name);
        getInvolved.setStartDate(startDate);
        getInvolved.setEndDate(endDate);
        getInvolved.setLink(link);

        projectService.saveOrUpdateGetInvolved(getInvolved);

        redirectAttributes.addFlashAttribute("success", "GetInvolved updated successfully!");
        return "redirect:/admin/getinvolved";
    }
}
