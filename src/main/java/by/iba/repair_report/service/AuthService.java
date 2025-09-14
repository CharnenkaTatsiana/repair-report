package by.iba.repair_report.service;

import by.iba.repair_report.dto.request.LoginRequest;
import by.iba.repair_report.dto.request.SignupRequest;
import by.iba.repair_report.dto.response.JwtResponse;

public interface AuthService {
    JwtResponse authenticateUser(LoginRequest loginRequest);
    void registerUser(SignupRequest signUpRequest);
    void validateUserAccess(Long userId);
}
