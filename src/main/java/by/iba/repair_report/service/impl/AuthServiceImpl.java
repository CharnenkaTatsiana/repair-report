package by.iba.repair_report.service.impl;

import by.iba.repair_report.dto.request.LoginRequest;
import by.iba.repair_report.dto.request.SignupRequest;
import by.iba.repair_report.dto.response.JwtResponse;
import by.iba.repair_report.entity.ERole;
import by.iba.repair_report.entity.User;
import by.iba.repair_report.repository.UserRepository;
import by.iba.repair_report.security.JwtUtils;
import by.iba.repair_report.security.UserDetailsImpl;
import by.iba.repair_report.service.AuthService;
import by.iba.repair_report.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           JwtUtils jwtUtils,
                           UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @Override
    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), roles);
    }

    @Override
    public void registerUser(SignupRequest signUpRequest) {
        userService.createUser(signUpRequest);
    }

    @Override
    public void validateUserAccess(Long userId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDetailsImpl userDetails = (UserDetailsImpl) authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, "")).getPrincipal();

        if (!userDetails.getId().equals(userId) &&
                !userDetails.getAuthorities().stream()
                        .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            throw new RuntimeException("Access denied");
        }
    }
}