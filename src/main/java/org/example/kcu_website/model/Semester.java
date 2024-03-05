package org.example.kcu_website.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "Semesters")
public class Semester {
  @jakarta.persistence.Id
  @Column(name = "semester_id")
  private Long id;
  private String semesterName;

  public Semester() {

  }

  public Semester(Long id, String semesterName) {
    this.id = id;
    this.semesterName = semesterName;
  }

  public Long getId() {
    return id;
  }

  public String getSemesterName() {
    return semesterName;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setSemesterName(String semesterName) {
    this.semesterName = semesterName;
  }
}
