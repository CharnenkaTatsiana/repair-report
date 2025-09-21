package by.iba.repair_report.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
public class PublicController {

    @GetMapping("/test")
    public String publicTest() {
        return "This is public content! Security is working!";
    }

    @GetMapping("/health")
    public String healthCheck() {
        return "Server is healthy!";
    }
}
