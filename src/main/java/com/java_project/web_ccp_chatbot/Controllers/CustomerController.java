package com.java_project.web_ccp_chatbot.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @GetMapping("/profile")
    public String customerProfile() {
        return "Welcome, Customer!";
    }



}
