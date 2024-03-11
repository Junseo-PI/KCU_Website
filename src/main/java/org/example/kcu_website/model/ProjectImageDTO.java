package org.example.kcu_website.model;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ProjectImageDTO {
    private Long id;
    private String name;
    private Long semesterId;
    private String level;
    private String languagesPlatforms;
    private String shortDescription;
    private String githubLink;
    private String longDescription;
    private String semesterName;

    private List<String> participantNames;
    private List<Participant> participants;
    private MultipartFile images_link1;
    private MultipartFile images_link2;
    private MultipartFile images_link3;

    public ProjectImageDTO() {
    }

    public ProjectImageDTO(Long id, String name, Long semesterId, String level, String languagesPlatforms, String shortDescription, String githubLink, String longDescription, String semesterName, List<String> participantNames, List<Participant> participants, MultipartFile images_link1, MultipartFile images_link2, MultipartFile images_link3) {
        this.id = id;
        this.name = name;
        this.semesterId = semesterId;
        this.level = level;
        this.languagesPlatforms = languagesPlatforms;
        this.shortDescription = shortDescription;
        this.githubLink = githubLink;
        this.longDescription = longDescription;
        this.semesterName = semesterName;
        this.participantNames = participantNames;
        this.participants = participants;
        this.images_link1 = images_link1;
        this.images_link2 = images_link2;
        this.images_link3 = images_link3;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getSemesterId() {
        return semesterId;
    }

    public String getLevel() {
        return level;
    }

    public String getLanguagesPlatforms() {
        return languagesPlatforms;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getGithubLink() {
        return githubLink;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public String getSemesterName() {
        return semesterName;
    }

    public List<String> getParticipantNames() {
        return participantNames;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public MultipartFile getImages_link1() {
        return images_link1;
    }

    public MultipartFile getImages_link2() {
        return images_link2;
    }

    public MultipartFile getImages_link3() {
        return images_link3;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSemesterId(Long semesterId) {
        this.semesterId = semesterId;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setLanguagesPlatforms(String languagesPlatforms) {
        this.languagesPlatforms = languagesPlatforms;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public void setGithubLink(String githubLink) {
        this.githubLink = githubLink;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public void setSemesterName(String semesterName) {
        this.semesterName = semesterName;
    }

    public void setParticipantNames(List<String> participantNames) {
        this.participantNames = participantNames;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    public void setImages_link1(MultipartFile images_link1) {
        this.images_link1 = images_link1;
    }

    public void setImages_link2(MultipartFile images_link2) {
        this.images_link2 = images_link2;
    }

    public void setImages_link3(MultipartFile images_link3) {
        this.images_link3 = images_link3;
    }
}
