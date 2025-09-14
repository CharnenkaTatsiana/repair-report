package by.iba.repair_report.controller.plan;

import by.iba.repair_report.dto.request.PlanRequest;
import by.iba.repair_report.dto.request.UpdatePlanStatusRequest;
import by.iba.repair_report.dto.response.PlanResponse;
import by.iba.repair_report.dto.response.PlanSummaryResponse;
import by.iba.repair_report.service.PlanService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plans")
public class PlanController {

    private final PlanService planService;

    public PlanController(PlanService planService) {
        this.planService = planService;
    }

    @PostMapping
    @PreAuthorize("hasRole('BRANCH_ENG') or hasRole('ENTERPRISE_ENG') or hasRole('ASSOCIATION_ENG')")
    public ResponseEntity<PlanResponse> createPlan(@Valid @RequestBody PlanRequest request) {
        PlanResponse response = planService.createPlan(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("(hasRole('BRANCH_ENG') and @securityService.isPlanInUserBranch(#id)) or " +
            "(hasRole('ENTERPRISE_ENG') and @securityService.isPlanInUserEnterprise(#id)) or " +
            "hasRole('ASSOCIATION_ENG')")
    public ResponseEntity<PlanResponse> updatePlan(@PathVariable Long id, @Valid @RequestBody PlanRequest request) {
        PlanResponse response = planService.updatePlan(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("(hasRole('BRANCH_ENG') and @securityService.isPlanInUserBranch(#id)) or " +
            "(hasRole('ENTERPRISE_ENG') and @securityService.isPlanInUserEnterprise(#id)) or " +
            "hasRole('ASSOCIATION_ENG')")
    public ResponseEntity<PlanResponse> updatePlanStatus(@PathVariable Long id, @Valid @RequestBody UpdatePlanStatusRequest request) {
        PlanResponse response = planService.updatePlanStatus(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("(hasRole('BRANCH_ENG') and @securityService.isPlanInUserBranch(#id)) or " +
            "(hasRole('ENTERPRISE_ENG') and @securityService.isPlanInUserEnterprise(#id)) or " +
            "hasRole('ASSOCIATION_ENG')")
    public ResponseEntity<Void> deletePlan(@PathVariable Long id) {
        planService.deletePlan(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("(hasRole('BRANCH_ENG') and @securityService.isPlanInUserBranch(#id)) or " +
            "(hasRole('ENTERPRISE_ENG') and @securityService.isPlanInUserEnterprise(#id)) or " +
            "hasRole('ASSOCIATION_ENG') or hasRole('USER')")
    public ResponseEntity<PlanResponse> getPlanById(@PathVariable Long id) {
        PlanResponse response = planService.getPlanById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/branch/{branchId}")
    @PreAuthorize("(hasRole('BRANCH_ENG') and @securityService.isUserInBranch(#branchId)) or " +
            "(hasRole('ENTERPRISE_ENG') and @securityService.isUserInEnterpriseOfBranch(#branchId)) or " +
            "hasRole('ASSOCIATION_ENG') or hasRole('USER')")
    public ResponseEntity<List<PlanResponse>> getPlansByBranch(@PathVariable Long branchId) {
        List<PlanResponse> responses = planService.getPlansByBranch(branchId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/enterprise/{enterpriseId}")
    @PreAuthorize("(hasRole('ENTERPRISE_ENG') and @securityService.isUserInEnterprise(#enterpriseId)) or " +
            "hasRole('ASSOCIATION_ENG') or hasRole('USER')")
    public ResponseEntity<List<PlanResponse>> getPlansByEnterprise(@PathVariable Long enterpriseId) {
        List<PlanResponse> responses = planService.getPlansByEnterprise(enterpriseId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/association/{associationId}")
    @PreAuthorize("hasRole('ASSOCIATION_ENG') or hasRole('USER')")
    public ResponseEntity<List<PlanResponse>> getPlansByAssociation(@PathVariable Long associationId) {
        List<PlanResponse> responses = planService.getPlansByAssociation(associationId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/branch/{branchId}/year/{year}")
    @PreAuthorize("(hasRole('BRANCH_ENG') and @securityService.isUserInBranch(#branchId)) or " +
            "(hasRole('ENTERPRISE_ENG') and @securityService.isUserInEnterpriseOfBranch(#branchId)) or " +
            "hasRole('ASSOCIATION_ENG') or hasRole('USER')")
    public ResponseEntity<PlanResponse> getPlanByBranchAndYear(@PathVariable Long branchId, @PathVariable Integer year) {
        PlanResponse response = planService.getPlanByBranchAndYear(branchId, year);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/summary")
    @PreAuthorize("(hasRole('BRANCH_ENG') and @securityService.isPlanInUserBranch(#id)) or " +
            "(hasRole('ENTERPRISE_ENG') and @securityService.isPlanInUserEnterprise(#id)) or " +
            "hasRole('ASSOCIATION_ENG') or hasRole('USER')")
    public ResponseEntity<PlanSummaryResponse> getPlanSummary(@PathVariable Long id) {
        PlanSummaryResponse response = planService.getPlanSummary(id);
        return ResponseEntity.ok(response);
    }
}
