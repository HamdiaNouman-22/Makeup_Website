package com.java_project.web_ccp_chatbot.repositories;

import com.java_project.web_ccp_chatbot.entities.Admin;
import com.java_project.web_ccp_chatbot.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
    Optional findByEmail(String email);
    Optional<Customer> findBycustomerId(UUID id);
}
