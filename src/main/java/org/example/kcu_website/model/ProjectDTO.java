package org.example.kcu_website.model;

import jakarta.persistence.Column;

import java.lang.reflect.Array;
import java.util.ArrayList;
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
  private List<Participant> participants;
  private String images_link1;
  private ArrayList<String> imagesLink;

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

  public void setParticipants(List<Participant> participants) {
    this.participants = participants;
  }

  public List<Participant> getParticipants() {
    return participants;
  }

  public void setImages_link1(String images_link1) {
    this.images_link1 = images_link1;
  }

  public String getImages_link1() {
    return images_link1;
  }

  public void setImagesLink(ArrayList<String> imagesLink) {
    this.imagesLink = imagesLink;
  }

  public ArrayList<String> getImagesLink() {
    return imagesLink;
  }
}
