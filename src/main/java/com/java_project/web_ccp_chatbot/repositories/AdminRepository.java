package com.java_project.web_ccp_chatbot.repositories;

import com.java_project.web_ccp_chatbot.entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByEmail(String email);
    Optional<Admin> findByadminId(UUID id);
}
