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

  @Column(name = "semester_name")
  private String name;

  public Semester() {

  }

  public Semester(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }
}
