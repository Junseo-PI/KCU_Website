package org.example.kcu_website.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "Projects")
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

  public Project() {

  }

  public Project(Long id, Long semesterId, String level, String languagesPlatforms, String name, String shortDescription, String githubLink, String longDescription) {
    this.id = id;
    this.semesterId = semesterId;
    this.level = level;
    this.languagesPlatforms = languagesPlatforms;
    this.name = name;
    this.shortDescription = shortDescription;
    this.githubLink = githubLink;
    this.longDescription = longDescription;
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


}
