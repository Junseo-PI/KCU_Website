package org.example.kcu_website.model;

import org.springframework.web.multipart.MultipartFile;

public class BannerImageDTO {
    private Long id;
    private String name;
    private MultipartFile imageLink;

    public BannerImageDTO() {
    }

    public BannerImageDTO(Long id, String name, MultipartFile imageLink) {
        this.id = id;
        this.name = name;
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

    public MultipartFile getImageLink() {
        return imageLink;
    }

    public void setImageLink(MultipartFile imageLink) {
        this.imageLink = imageLink;
    }
}
