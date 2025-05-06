package com.java_project.web_ccp_chatbot.Services;

import com.java_project.web_ccp_chatbot.entities.Admin;
import com.java_project.web_ccp_chatbot.entities.Customer;
import com.java_project.web_ccp_chatbot.entities.DTO.UserDetailsDTO;
import com.java_project.web_ccp_chatbot.repositories.AdminRepository;
import com.java_project.web_ccp_chatbot.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class SuperbaseService {

    @Autowired
    private AdminRepository adminRepo;

    @Autowired
    private CustomerRepository customerRepo;

    public String getRoleByEmail(String email) {
        if (adminRepo.findByEmail(email).isPresent()) {
            return "ADMIN";
        } else if (customerRepo.findByEmail(email).isPresent()) {
            return "CUSTOMER";
        }
        return null;
    }

    public UserDetailsDTO getUserDetailsById(UUID userId) {
        Optional<Admin> admin = adminRepo.findByadminId(userId);
        if (admin.isPresent()) {
            Admin a = admin.get();
            return new UserDetailsDTO(a.getAdminId(), a.getName(), a.getEmail(), "ADMIN");
        }

        // Check Customer
        Optional<Customer> customer = customerRepo.findBycustomerId(userId);
        if (customer.isPresent()) {
            Customer c = customer.get();
            return new UserDetailsDTO(
                    c.getCustomerID(),
                    c.getName(),
                    c.getEmail(),
                    "CUSTOMER",
                    c.getPhone(),     // assuming getter exists
                    c.getAddress()    // assuming getter exists
            );
        }

        return null;
    }


}
