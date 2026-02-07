package com.alexportfolio;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/resume")
    public String resume() {
        return "resume";
    }

    @GetMapping("/projects")
    public String projects() {
        return "projects";
    }
}