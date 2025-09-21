package by.iba.repair_report.controller.auth;

import by.iba.repair_report.dto.request.LoginRequest;
import by.iba.repair_report.dto.request.SignupRequest;
import by.iba.repair_report.dto.response.JwtResponse;
import by.iba.repair_report.dto.response.MessageResponse;
import by.iba.repair_report.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        JwtResponse jwtResponse = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        authService.registerUser(signUpRequest);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
    @GetMapping("/test")
    public String testAuth() {
        return "Public Content from Auth!";
    }

    @GetMapping("/health")
    public String healthCheck() {
        return "Auth endpoint is working!";
    }

}
