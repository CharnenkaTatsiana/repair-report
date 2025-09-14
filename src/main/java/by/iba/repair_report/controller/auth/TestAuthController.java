package by.iba.repair_report.controller.auth;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestAuthController {

    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('BRANCH_ENG') or hasRole('ENTERPRISE_ENG') or hasRole('ASSOCIATION_ENG') or hasRole('ADMIN')")
    public String userAccess() {
        return "User Content.";
    }

    @GetMapping("/branch")
    @PreAuthorize("hasRole('BRANCH_ENG')")
    public String branchEngAccess() {
        return "Branch Engineer Board.";
    }

    @GetMapping("/enterprise")
    @PreAuthorize("hasRole('ENTERPRISE_ENG')")
    public String enterpriseEngAccess() {
        return "Enterprise Engineer Board.";
    }

    @GetMapping("/association")
    @PreAuthorize("hasRole('ASSOCIATION_ENG')")
    public String associationEngAccess() {
        return "Association Engineer Board.";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Admin Board.";
    }
}