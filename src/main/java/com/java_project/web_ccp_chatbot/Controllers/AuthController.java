package com.java_project.web_ccp_chatbot.Controllers;

import com.java_project.web_ccp_chatbot.JwtUtil;
import com.java_project.web_ccp_chatbot.Services.SuperbaseService;
import com.java_project.web_ccp_chatbot.Services.UserContextService;
import com.java_project.web_ccp_chatbot.entities.Admin;
import com.java_project.web_ccp_chatbot.entities.Customer;
import com.java_project.web_ccp_chatbot.entities.DTO.LoginDTO;
import com.java_project.web_ccp_chatbot.entities.DTO.SignupDTO;
import com.java_project.web_ccp_chatbot.repositories.AdminRepository;
import com.java_project.web_ccp_chatbot.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AdminRepository adminRepo;

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserContextService userContextService;
    @Autowired
    private SuperbaseService service;

    @PostMapping("/signup/admin")
    public ResponseEntity<?> signupAdmin(@RequestBody SignupDTO request) {
        if (adminRepo.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Admin already exists");
        }

        Admin admin = new Admin();
        admin.setName(request.getName());
        admin.setEmail(request.getEmail());
        admin.setPassword(passwordEncoder.encode(request.getPassword())); // Use BCrypt in production

        adminRepo.save(admin);

        return ResponseEntity.ok("Admin registered successfully");
    }

    @PostMapping("/signup/customer")
    public ResponseEntity<?> signupCustomer(@RequestBody SignupDTO request) {
        if (customerRepo.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Customer already exists");
        }

        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPassword(passwordEncoder.encode(request.getPassword())); // Use BCrypt
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());

        customerRepo.save(customer);

        return ResponseEntity.ok("Customer registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO request) {
        // Check in admin table
        Optional<Admin> admin = adminRepo.findByEmail(request.getEmail());
        if (admin.isPresent() && passwordEncoder.matches(request.getPassword(), admin.get().getPassword())) {
            String token = jwtUtil.generateToken(admin.get().getAdminId().toString(), "ADMIN");
            return ResponseEntity.ok(Collections.singletonMap("token", token));
        }

        // Check in customer table
        Optional<Customer> customer = customerRepo.findByEmail(request.getEmail());
        if (customer.isPresent() && passwordEncoder.matches(request.getPassword(), customer.get().getPassword())) {
            String token = jwtUtil.generateToken(customer.get().getCustomerID().toString(), "CUSTOMER");
            return ResponseEntity.ok(Collections.singletonMap("token", token));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    @GetMapping("/me")
    public ResponseEntity<?> getLoggedInUserInfo() {
        UUID userId = userContextService.getCurrentUserId(); // Fetch user ID instead of email
        System.out.println(userId); // Optionally log user ID for debugging
        return ResponseEntity.ok(service.getUserDetailsById(userId)); // Use getUserDetailsById to fetch details by user ID
    }
}
