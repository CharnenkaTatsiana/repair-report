package by.iba.repair_report.service;

import by.iba.repair_report.dto.request.PlanRequest;
import by.iba.repair_report.dto.request.UpdatePlanStatusRequest;
import by.iba.repair_report.dto.response.PlanResponse;
import by.iba.repair_report.dto.response.PlanSummaryResponse;

import java.util.List;

public interface PlanService {
    PlanResponse createPlan(PlanRequest request);
    PlanResponse updatePlan(Long planId, PlanRequest request);
    PlanResponse updatePlanStatus(Long planId, UpdatePlanStatusRequest request);
    void deletePlan(Long planId);
    PlanResponse getPlanById(Long planId);
    List<PlanResponse> getPlansByBranch(Long branchId);
    List<PlanResponse> getPlansByEnterprise(Long enterpriseId);
    List<PlanResponse> getPlansByAssociation(Long associationId);
    PlanResponse getPlanByBranchAndYear(Long branchId, Integer year);
    PlanSummaryResponse getPlanSummary(Long planId);
    boolean existsPlanForBranchAndYear(Long branchId, Integer year);
}
