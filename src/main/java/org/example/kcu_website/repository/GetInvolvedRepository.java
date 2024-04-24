package org.example.kcu_website.repository;

import org.example.kcu_website.model.GetInvolved;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GetInvolvedRepository  extends JpaRepository<GetInvolved, Long> {
    Optional<GetInvolved> findFirstByOrderByStartDateDesc();
}
