package org.example.kcu_website.repository;

import org.example.kcu_website.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
  List<Project> findBySemesterId(Long semesterId);
}
