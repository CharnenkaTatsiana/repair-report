package by.iba.repair_report.service.impl;

import by.iba.repair_report.dto.request.SignupRequest;
import by.iba.repair_report.dto.response.UserDto;
import by.iba.repair_report.entity.ERole;
import by.iba.repair_report.entity.Role;
import by.iba.repair_report.entity.User;
import by.iba.repair_report.repository.*;
import by.iba.repair_report.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BranchRepository branchRepository;
    private final EnterpriseRepository enterpriseRepository;
    private final AssociationRepository associationRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           BranchRepository branchRepository, EnterpriseRepository enterpriseRepository,
                           AssociationRepository associationRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.branchRepository = branchRepository;
        this.enterpriseRepository = enterpriseRepository;
        this.associationRepository = associationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User createUser(SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new RuntimeException("Username is already taken!");
        }

        User user = new User(signUpRequest.getUsername(),
                passwordEncoder.encode(signUpRequest.getPassword()));

        Set<Role> roles = resolveRoles(signUpRequest.getRoles());
        user.setRoles(roles);

        resolveUserAssociations(user, signUpRequest);

        return userRepository.save(user);
    }

    private Set<Role> resolveRoles(Set<String> strRoles) {
        if (strRoles == null || strRoles.isEmpty()) {
            return Set.of(roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role USER not found.")));
        }

        return strRoles.stream()
                .map(role -> switch (role.toLowerCase()) {
                    case "admin" -> roleRepository.findByName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: Role ADMIN not found."));
                    case "association_eng" -> roleRepository.findByName(ERole.ROLE_ASSOCIATION_ENG)
                            .orElseThrow(() -> new RuntimeException("Error: Role ASSOCIATION_ENG not found."));
                    case "enterprise_eng" -> roleRepository.findByName(ERole.ROLE_ENTERPRISE_ENG)
                            .orElseThrow(() -> new RuntimeException("Error: Role ENTERPRISE_ENG not found."));
                    case "branch_eng" -> roleRepository.findByName(ERole.ROLE_BRANCH_ENG)
                            .orElseThrow(() -> new RuntimeException("Error: Role BRANCH_ENG not found."));
                    default -> roleRepository.findByName(ERole.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Error: Role USER not found."));
                })
                .collect(Collectors.toSet());
    }

    private void resolveUserAssociations(User user, SignupRequest signUpRequest) {
        if (signUpRequest.getBranchId() != null) {
            user.setBranch(branchRepository.findById(signUpRequest.getBranchId())
                    .orElseThrow(() -> new ResourceNotFoundException("Branch not found")));
        }

        if (signUpRequest.getEnterpriseId() != null) {
            user.setEnterprise(enterpriseRepository.findById(signUpRequest.getEnterpriseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Enterprise not found")));
        }
    }

    @Override
    @Transactional
    public UserDto updateUser(Long userId, SignupRequest signUpRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!user.getUsername().equals(signUpRequest.getUsername()) &&
                userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new RuntimeException("Username is already taken!");
        }

        user.setUsername(signUpRequest.getUsername());

        if (signUpRequest.getPassword() != null && !signUpRequest.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        }

        Set<Role> roles = resolveRoles(signUpRequest.getRoles());
        user.setRoles(roles);

        resolveUserAssociations(user, signUpRequest);

        User updatedUser = userRepository.save(user);
        return convertToDto(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userRepository.delete(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return convertToDto(user);
    }

    @Override
    @Transactional
    public void changePassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public UserDto getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return convertToDto(user);
    }

    @Override
    public boolean hasAccessToBranch(Long userId, Long branchId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return user.getBranch() != null && user.getBranch().getId().equals(branchId);
    }

    @Override
    public boolean hasAccessToEnterprise(Long userId, Long enterpriseId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return user.getEnterprise() != null && user.getEnterprise().getId().equals(enterpriseId);
    }

    private UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setRoles(user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet()));

        if (user.getBranch() != null) {
            dto.setBranchId(user.getBranch().getId());
            dto.setBranchName(user.getBranch().getName());
        }

        if (user.getEnterprise() != null) {
            dto.setEnterpriseId(user.getEnterprise().getId());
            dto.setEnterpriseName(user.getEnterprise().getName());

            if (user.getEnterprise().getAssociation() != null) {
                dto.setAssociationId(user.getEnterprise().getAssociation().getId());
                dto.setAssociationName(user.getEnterprise().getAssociation().getName());
            }
        }

        return dto;
    }
}
