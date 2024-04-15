package org.example.kcu_website.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "projects")
public class Project {
  @jakarta.persistence.Id
  @Column(name = "project_id")
  private Long id;
  private Long semesterId;
  private String level;
  private String languagesPlatforms;
  private String name;
  private String shortDescription;
  private String githubLink;
  private String longDescription;
  private String images_link1;
  private String images_link2;
  private String images_link3;

  public Project() {

  }

  public Project(Long id, Long semesterId, String level, String languagesPlatforms, String name, String shortDescription, String githubLink, String longDescription, String images_link1, String images_link2, String images_link3) {
    this.id = id;
    this.semesterId = semesterId;
    this.level = level;
    this.languagesPlatforms = languagesPlatforms;
    this.name = name;
    this.shortDescription = shortDescription;
    this.githubLink = githubLink;
    this.longDescription = longDescription;
    this.images_link1 = images_link1;
    this.images_link2 = images_link2;
    this.images_link3 = images_link3;
  }

  public Long getId() {
    return id;
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

  public String getName() {
    return name;
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

  public void setId(Long id) {
    this.id = id;
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

  public void setName(String name) {
    this.name = name;
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

  public void setImages_link1(String images_link1) {
    this.images_link1 = images_link1;
  }

  public void setImages_link2(String images_link2) {
    this.images_link2 = images_link2;
  }

  public void setImages_link3(String images_link3) {
    this.images_link3 = images_link3;
  }

  public String getImages_link1() {
    return images_link1;
  }

  public String getImages_link2() {
    return images_link2;
  }

  public String getImages_link3() {
    return images_link3;
  }
}
