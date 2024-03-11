package org.example.kcu_website.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Participants")
public class Participant {
  @jakarta.persistence.Id
  @Column(name = "participant_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Long projectId;
  private String name;
  private String role;
  private String email;

  public Participant() {

  }

  public Participant(Long id, Long projectId, String name, String role, String email) {
    this.id = id;
    this.projectId = projectId;
    this.name = name;
    this.role = role;
    this.email = email;
  }

  // Getter
  public Long getId() {
    return id;
  }

  public Long getProjectId() {
    return projectId;
  }

  public String getName() {
    return name;
  }

  public String getRole() {
    return role;
  }

  public String getEmail() {
    return email;
  }

  // Setter
  public void setId(Long id) {
    this.id = id;
  }

  public void setProjectId(Long projectId) {
    this.projectId = projectId;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
