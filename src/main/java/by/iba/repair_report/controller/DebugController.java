package by.iba.repair_report.controller;

import by.iba.repair_report.entity.User;
import by.iba.repair_report.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/debug")
public class DebugController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DebugController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Endpoint для сброса пароля
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        try {
            String username = request.get("username");
            String newPassword = request.get("password");

            if (username == null || newPassword == null) {
                return ResponseEntity.badRequest().body("Username and password are required");
            }

            Optional<User> userOptional = userRepository.findByUsername(username);
            if (userOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            User user = userOptional.get();
            String encodedPassword = passwordEncoder.encode(newPassword);
            user.setPassword(encodedPassword);
            userRepository.save(user);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Password reset successfully");
            response.put("username", username);
            response.put("newPasswordHash", encodedPassword);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error resetting password: " + e.getMessage());
        }
    }

    // Endpoint для проверки пользователя
    @GetMapping("/user/{username}")
    public ResponseEntity<?> getUserInfo(@PathVariable String username) {
        try {
            Optional<User> userOptional = userRepository.findByUsername(username);
            if (userOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            User user = userOptional.get();
            Map<String, Object> response = new HashMap<>();
            response.put("username", user.getUsername());
            response.put("passwordHash", user.getPassword());
            response.put("roles", user.getRoles().stream()
                    .map(role -> role.getName().name())
                    .toList());
            response.put("userId", user.getId());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error getting user info: " + e.getMessage());
        }
    }

    // Endpoint для проверки пароля
    @PostMapping("/check-password")
    public ResponseEntity<?> checkPassword(@RequestBody Map<String, String> request) {
        try {
            String username = request.get("username");
            String password = request.get("password");

            if (username == null || password == null) {
                return ResponseEntity.badRequest().body("Username and password are required");
            }

            Optional<User> userOptional = userRepository.findByUsername(username);
            if (userOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            User user = userOptional.get();
            boolean matches = passwordEncoder.matches(password, user.getPassword());

            Map<String, Object> response = new HashMap<>();
            response.put("username", username);
            response.put("passwordMatches", matches);
            response.put("storedPasswordHash", user.getPassword());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error checking password: " + e.getMessage());
        }
    }

    // Endpoint для создания тестового пользователя
    @PostMapping("/create-test-user")
    public ResponseEntity<?> createTestUser(@RequestBody Map<String, String> request) {
        try {
            String username = request.get("username");
            String password = request.get("password");
            String roles = request.get("roles"); // "admin,user"

            if (username == null || password == null) {
                return ResponseEntity.badRequest().body("Username and password are required");
            }

            if (userRepository.existsByUsername(username)) {
                return ResponseEntity.badRequest().body("User already exists");
            }

            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));

            // Здесь нужно добавить логику для установки ролей
            // В зависимости от вашей реализации User entity

            userRepository.save(user);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Test user created successfully");
            response.put("username", username);
            response.put("passwordHash", user.getPassword());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error creating test user: " + e.getMessage());
        }
    }
}
