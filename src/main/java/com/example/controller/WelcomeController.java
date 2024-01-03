package com.example.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {

    @GetMapping("/Main")
    public String welcome(Model model) {
        model.addAttribute("message", "Welcome to our website! Test");
        return "welcome";
    }
}
