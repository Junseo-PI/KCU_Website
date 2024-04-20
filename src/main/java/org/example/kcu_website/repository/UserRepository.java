package org.example.kcu_website.repository;

import org.example.kcu_website.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByName(String username);

    User findFirstByOrderByIdDesc();
}
