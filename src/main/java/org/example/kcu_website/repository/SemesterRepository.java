package org.example.kcu_website.repository;
import org.example.kcu_website.model.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SemesterRepository extends JpaRepository<Semester, Long> {
  Optional<Semester> findTopByOrderByNameDesc();
  Optional<Semester> findByName(String semesterName);
}
