package by.iba.repair_report.service;

import by.iba.repair_report.entity.Plan;
import by.iba.repair_report.entity.Report;
import by.iba.repair_report.entity.User;
import by.iba.repair_report.repository.PlanRepository;
import by.iba.repair_report.repository.ReportRepository;
import by.iba.repair_report.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    private final UserRepository userRepository;
    private final PlanRepository planRepository;
    private final ReportRepository reportRepository;

    public SecurityService(UserRepository userRepository, PlanRepository planRepository, ReportRepository reportRepository) {
        this.userRepository = userRepository;
        this.planRepository = planRepository;
        this.reportRepository = reportRepository;
    }

    public boolean isUserInBranch(Long branchId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getBranch() != null && user.getBranch().getId().equals(branchId);
    }

    public boolean isUserInEnterprise(Long enterpriseId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getEnterprise() != null && user.getEnterprise().getId().equals(enterpriseId);
    }

    public boolean isUserInEnterpriseOfBranch(Long branchId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        // Логика проверки, что пользователь принадлежит предприятию, которому принадлежит филиал
        return user.getEnterprise() != null &&
                user.getEnterprise().getBranches().stream()
                        .anyMatch(branch -> branch.getId().equals(branchId));
    }

    public boolean isPlanInUserBranch(Long planId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found"));
        return user.getBranch() != null &&
                user.getBranch().getId().equals(plan.getBranch().getId());
    }

    public boolean isPlanInUserEnterprise(Long planId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found"));
        return user.getEnterprise() != null &&
                user.getEnterprise().getId().equals(plan.getBranch().getEnterprise().getId());
    }

    public boolean isReportInUserBranch(Long reportId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));
        return user.getBranch() != null &&
                user.getBranch().getId().equals(report.getBranch().getId());
    }

    public boolean isReportInUserEnterprise(Long reportId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));
        return user.getEnterprise() != null &&
                user.getEnterprise().getId().equals(report.getBranch().getEnterprise().getId());
    }
}
