package by.iba.repair_report.controller.report;

import by.iba.repair_report.dto.request.ReportRequest;
import by.iba.repair_report.dto.request.UpdateReportStatusRequest;
import by.iba.repair_report.dto.response.ReportResponse;
import by.iba.repair_report.dto.response.ReportSummaryResponse;
import by.iba.repair_report.service.ReportService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping
    @PreAuthorize("hasRole('BRANCH_ENG')")
    public ResponseEntity<ReportResponse> createReport(@Valid @RequestBody ReportRequest request) {
        ReportResponse response = reportService.createReport(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("(hasRole('BRANCH_ENG') and @securityService.isReportInUserBranch(#id)) or " +
            "(hasRole('ENTERPRISE_ENG') and @securityService.isReportInUserEnterprise(#id)) or " +
            "hasRole('ASSOCIATION_ENG')")
    public ResponseEntity<ReportResponse> updateReport(@PathVariable Long id, @Valid @RequestBody ReportRequest request) {
        ReportResponse response = reportService.updateReport(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("(hasRole('BRANCH_ENG') and @securityService.isReportInUserBranch(#id)) or " +
            "(hasRole('ENTERPRISE_ENG') and @securityService.isReportInUserEnterprise(#id)) or " +
            "hasRole('ASSOCIATION_ENG')")
    public ResponseEntity<ReportResponse> updateReportStatus(@PathVariable Long id, @Valid @RequestBody UpdateReportStatusRequest request) {
        ReportResponse response = reportService.updateReportStatus(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("(hasRole('BRANCH_ENG') and @securityService.isReportInUserBranch(#id)) or " +
            "(hasRole('ENTERPRISE_ENG') and @securityService.isReportInUserEnterprise(#id)) or " +
            "hasRole('ASSOCIATION_ENG')")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        reportService.deleteReport(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("(hasRole('BRANCH_ENG') and @securityService.isReportInUserBranch(#id)) or " +
            "(hasRole('ENTERPRISE_ENG') and @securityService.isReportInUserEnterprise(#id)) or " +
            "hasRole('ASSOCIATION_ENG') or hasRole('USER')")
    public ResponseEntity<ReportResponse> getReportById(@PathVariable Long id) {
        ReportResponse response = reportService.getReportById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/branch/{branchId}")
    @PreAuthorize("(hasRole('BRANCH_ENG') and @securityService.isUserInBranch(#branchId)) or " +
            "(hasRole('ENTERPRISE_ENG') and @securityService.isUserInEnterpriseOfBranch(#branchId)) or " +
            "hasRole('ASSOCIATION_ENG') or hasRole('USER')")
    public ResponseEntity<List<ReportResponse>> getReportsByBranch(@PathVariable Long branchId) {
        List<ReportResponse> responses = reportService.getReportsByBranch(branchId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/branch/{branchId}/year/{year}")
    @PreAuthorize("(hasRole('BRANCH_ENG') and @securityService.isUserInBranch(#branchId)) or " +
            "(hasRole('ENTERPRISE_ENG') and @securityService.isUserInEnterpriseOfBranch(#branchId)) or " +
            "hasRole('ASSOCIATION_ENG') or hasRole('USER')")
    public ResponseEntity<List<ReportResponse>> getReportsByBranchAndYear(@PathVariable Long branchId, @PathVariable Integer year) {
        List<ReportResponse> responses = reportService.getReportsByBranchAndYear(branchId, year);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/enterprise/{enterpriseId}")
    @PreAuthorize("(hasRole('ENTERPRISE_ENG') and @securityService.isUserInEnterprise(#enterpriseId)) or " +
            "hasRole('ASSOCIATION_ENG') or hasRole('USER')")
    public ResponseEntity<List<ReportResponse>> getReportsByEnterprise(@PathVariable Long enterpriseId) {
        List<ReportResponse> responses = reportService.getReportsByEnterprise(enterpriseId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/association/{associationId}")
    @PreAuthorize("hasRole('ASSOCIATION_ENG') or hasRole('USER')")
    public ResponseEntity<List<ReportResponse>> getReportsByAssociation(@PathVariable Long associationId) {
        List<ReportResponse> responses = reportService.getReportsByAssociation(associationId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/branch/{branchId}/period/{period}")
    @PreAuthorize("(hasRole('BRANCH_ENG') and @securityService.isUserInBranch(#branchId)) or " +
            "(hasRole('ENTERPRISE_ENG') and @securityService.isUserInEnterpriseOfBranch(#branchId)) or " +
            "hasRole('ASSOCIATION_ENG') or hasRole('USER')")
    public ResponseEntity<ReportResponse> getReportByBranchAndPeriod(@PathVariable Long branchId, @PathVariable YearMonth period) {
        ReportResponse response = reportService.getReportByBranchAndPeriod(branchId, period);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/summary")
    @PreAuthorize("(hasRole('BRANCH_ENG') and @securityService.isReportInUserBranch(#id)) or " +
            "(hasRole('ENTERPRISE_ENG') and @securityService.isReportInUserEnterprise(#id)) or " +
            "hasRole('ASSOCIATION_ENG') or hasRole('USER')")
    public ResponseEntity<ReportSummaryResponse> getReportSummary(@PathVariable Long id) {
        ReportSummaryResponse response = reportService.getReportSummary(id);
        return ResponseEntity.ok(response);
    }
}
