package org.example.kcu_website.model;

import org.springframework.web.multipart.MultipartFile;

public class LeaderImageDTO {
    private Long id;
    private String name;
    private String startSemester;
    private String endSemester;
    private String email;
    private String introduction;
    private MultipartFile imageLink;

    public LeaderImageDTO() {
    }

    public LeaderImageDTO(Long id, String name, String startSemester, String endSemester, String email, String introduction, MultipartFile imageLink) {
        this.id = id;
        this.name = name;
        this.startSemester = startSemester;
        this.endSemester = endSemester;
        this.email = email;
        this.introduction = introduction;
        this.imageLink = imageLink;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartSemester() {
        return startSemester;
    }

    public void setStartSemester(String startSemester) {
        this.startSemester = startSemester;
    }

    public String getEndSemester() {
        return endSemester;
    }

    public void setEndSemester(String endSemester) {
        this.endSemester = endSemester;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public MultipartFile getImageLink() {
        return imageLink;
    }

    public void setImageLink(MultipartFile imageLink) {
        this.imageLink = imageLink;
    }
}
