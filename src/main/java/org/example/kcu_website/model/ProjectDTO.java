package org.example.kcu_website.model;

import jakarta.persistence.Column;

import java.util.List;

public class ProjectDTO {
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

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public List<String> getParticipantNames() {
    return participantNames;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setParticipantNames(List<String> participantNames) {
    this.participantNames = participantNames;
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

  public String getSemesterName() {
    return semesterName;
  }
}
