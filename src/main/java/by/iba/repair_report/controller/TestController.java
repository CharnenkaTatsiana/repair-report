package by.iba.repair_report.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/api/test-simple")
    public String testSimple() {
        return "Simple test - works!";
    }

    @GetMapping("/test")
    public String testRoot() {
        return "Root test - works!";
    }
}
