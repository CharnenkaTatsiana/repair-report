package by.iba.repair_report.service;

import by.iba.repair_report.dto.request.SignupRequest;
import by.iba.repair_report.dto.response.UserDto;
import by.iba.repair_report.entity.User;

import java.util.List;

public interface UserService {
    User createUser(SignupRequest signUpRequest);
    UserDto updateUser(Long userId, SignupRequest signUpRequest);
    void deleteUser(Long userId);
    List<UserDto> getAllUsers();
    UserDto getUserById(Long userId);
    void changePassword(Long userId, String newPassword);
    UserDto getCurrentUser();
    boolean hasAccessToBranch(Long userId, Long branchId);
    boolean hasAccessToEnterprise(Long userId, Long enterpriseId);
}