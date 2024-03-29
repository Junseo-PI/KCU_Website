package org.example.kcu_website.repository;

import org.example.kcu_website.model.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
  List<Participant> findByProjectId(Long projectId);
}
