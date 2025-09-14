package by.iba.repair_report.service.impl;



import by.iba.repair_report.dto.request.PlanRequest;
import by.iba.repair_report.dto.request.UpdatePlanStatusRequest;
import by.iba.repair_report.dto.response.PlanItemResponse;
import by.iba.repair_report.dto.response.PlanResponse;
import by.iba.repair_report.dto.response.PlanSummaryResponse;
import by.iba.repair_report.entity.*;


import by.iba.repair_report.exception.ResourceNotFoundException;
import by.iba.repair_report.repository.*;

import by.iba.repair_report.service.PlanService;
import by.iba.repair_report.service.WorkCategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PlanServiceImpl implements PlanService {

    private final PlanRepository planRepository;
    private final PlanItemRepository planItemRepository;
    private final BranchRepository branchRepository;
    private final WorkCategoryRepository workCategoryRepository;
    private final WorkCategoryService workCategoryService;

    public PlanServiceImpl(PlanRepository planRepository,
                           PlanItemRepository planItemRepository,
                           BranchRepository branchRepository,
                           WorkCategoryRepository workCategoryRepository,
                           WorkCategoryService workCategoryService) {
        this.planRepository = planRepository;
        this.planItemRepository = planItemRepository;
        this.branchRepository = branchRepository;
        this.workCategoryRepository = workCategoryRepository;
        this.workCategoryService = workCategoryService;
    }

    @Override
    @Transactional
    public PlanResponse createPlan(PlanRequest request) {
        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id: " + request.getBranchId()));

        if (planRepository.existsByBranchIdAndYear(request.getBranchId(), request.getYear())) {
            throw new RuntimeException("Plan for branch " + branch.getName() + " and year " + request.getYear() + " already exists");
        }

        Plan plan = new Plan();
        plan.setBranch(branch);
        plan.setYear(Year.of(request.getYear())); // Убедитесь, что здесь тоже Year.of()
        plan.setStatus(Plan.PlanStatus.DRAFT);

        Plan savedPlan = planRepository.save(plan);
        createPlanItems(savedPlan, request.getPlanItems());

        return convertToResponse(savedPlan);
    }

    private void createPlanItems(Plan plan, Map<Long, PlanRequest.QuarterValues> planItems) {
        if (planItems != null && !planItems.isEmpty()) {
            for (Map.Entry<Long, PlanRequest.QuarterValues> entry : planItems.entrySet()) {
                Long workCategoryId = entry.getKey();
                PlanRequest.QuarterValues values = entry.getValue();

                WorkCategory workCategory = workCategoryRepository.findById(workCategoryId)
                        .orElseThrow(() -> new ResourceNotFoundException("Work category not found with id: " + workCategoryId));

                if (!workCategory.getIsActive()) {
                    throw new RuntimeException("Work category " + workCategory.getName() + " is not active");
                }

                PlanItem planItem = new PlanItem();
                planItem.setPlan(plan);
                planItem.setWorkCategory(workCategory);
                planItem.setQ1Plan(values.getQ1() != null ? values.getQ1() : 0.0);
                planItem.setQ2Plan(values.getQ2() != null ? values.getQ2() : 0.0);
                planItem.setQ3Plan(values.getQ3() != null ? values.getQ3() : 0.0);
                planItem.setQ4Plan(values.getQ4() != null ? values.getQ4() : 0.0);
                planItem.calculateAnnualPlan();

                planItemRepository.save(planItem);
            }
        }
    }

    @Override
    @Transactional
    public PlanResponse updatePlan(Long planId, PlanRequest request) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found with id: " + planId));

        // Проверяем, изменился ли филиал
        if (!plan.getBranch().getId().equals(request.getBranchId())) {
            Branch branch = branchRepository.findById(request.getBranchId())
                    .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id: : " + request.getBranchId()));
            plan.setBranch(branch);
        }

        // Проверяем, изменился ли год - ИСПРАВЛЕННАЯ ЧАСТЬ
        if (plan.getYear().getValue() != request.getYear()) {
            // Проверяем, нет ли уже плана для этого филиала и года
            if (planRepository.existsByBranchIdAndYearAndIdNot(
                    request.getBranchId(), request.getYear(), planId)) {
                throw new RuntimeException("Plan for this branch and year already exists");
            }
            plan.setYear(Year.of(request.getYear()));
        }

        // Удаляем старые items и создаем новые
        planItemRepository.deleteByPlanId(planId);
        createPlanItems(plan, request.getPlanItems());

        Plan updatedPlan = planRepository.save(plan);
        return convertToResponse(updatedPlan);
    }

    @Override
    @Transactional
    public PlanResponse updatePlanStatus(Long planId, UpdatePlanStatusRequest request) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found with id: " + planId));

        plan.setStatus(request.getStatus());
        Plan updatedPlan = planRepository.save(plan);
        return convertToResponse(updatedPlan);
    }

    @Override
    @Transactional
    public void deletePlan(Long planId) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found with id: " + planId));

        // Проверяем, есть ли связанные отчеты
        // Можно добавить дополнительную проверку здесь

        planItemRepository.deleteByPlanId(planId);
        planRepository.delete(plan);
    }

    @Override
    public PlanResponse getPlanById(Long planId) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found with id: " + planId));
        return convertToResponse(plan);
    }

    @Override
    public List<PlanResponse> getPlansByBranch(Long branchId) {
        if (!branchRepository.existsById(branchId)) {
            throw new ResourceNotFoundException("Branch not found with id: " + branchId);
        }

        return planRepository.findByBranchId(branchId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlanResponse> getPlansByEnterprise(Long enterpriseId) {
        return planRepository.findByEnterpriseId(enterpriseId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlanResponse> getPlansByAssociation(Long associationId) {
        return planRepository.findByAssociationId(associationId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PlanResponse getPlanByBranchAndYear(Long branchId, Integer year) {
        return planRepository.findByBranchIdAndYear(branchId, year)
                .map(this::convertToResponse)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Plan not found for branch id: " + branchId + " and year: " + year));
    }

    @Override
    public PlanSummaryResponse getPlanSummary(Long planId) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found with id: " + planId));

        List<PlanItem> planItems = planItemRepository.findByPlanId(planId);

        // Группируем по типу сети и вычисляем итоги
        Map<String, Double> totalByNetworkType = planItems.stream()
                .collect(Collectors.groupingBy(
                        item -> item.getWorkCategory().getNetworkType().name(),
                        Collectors.summingDouble(PlanItem::getAnnualPlan)
                ));

        double grandTotal = planItems.stream()
                .mapToDouble(PlanItem::getAnnualPlan)
                .sum();

        PlanSummaryResponse summary = new PlanSummaryResponse();
        summary.setBranchId(plan.getBranch().getId());
        summary.setBranchName(plan.getBranch().getName());
        summary.setYear(plan.getYear().getValue());
        summary.setTotalByNetworkType(totalByNetworkType);
        summary.setGrandTotal(grandTotal);

        return summary;
    }

    @Override
    public boolean existsPlanForBranchAndYear(Long branchId, Integer year) {
        return planRepository.existsByBranchIdAndYear(branchId, year);
    }

    private PlanResponse convertToResponse(Plan plan) {
        PlanResponse response = new PlanResponse();
        response.setId(plan.getId());
        response.setBranchId(plan.getBranch().getId());
        response.setBranchName(plan.getBranch().getName());
        response.setYear(plan.getYear().getValue());
        response.setStatus(plan.getStatus().name());

        // Получаем все items для этого плана
        List<PlanItem> planItems = planItemRepository.findByPlanId(plan.getId());
        List<PlanItemResponse> itemResponses = planItems.stream()
                .map(this::convertToItemResponse)
                .collect(Collectors.toList());

        response.setPlanItems(itemResponses);
        return response;
    }

    private PlanItemResponse convertToItemResponse(PlanItem planItem) {
        PlanItemResponse response = new PlanItemResponse();
        response.setWorkCategoryId(planItem.getWorkCategory().getId());
        response.setWorkName(planItem.getWorkCategory().getName());
        response.setNetworkType(planItem.getWorkCategory().getNetworkType().name());
        response.setQ1(planItem.getQ1Plan());
        response.setQ2(planItem.getQ2Plan());
        response.setQ3(planItem.getQ3Plan());
        response.setQ4(planItem.getQ4Plan());
        response.setAnnual(planItem.getAnnualPlan());
        return response;
    }

    // Дополнительные методы для бизнес-логики

    @Transactional
    public void recalculatePlanAnnualTotal(Long planId) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found"));

        List<PlanItem> planItems = planItemRepository.findByPlanId(planId);
        for (PlanItem item : planItems) {
            item.calculateAnnualPlan();
            planItemRepository.save(item);
        }
    }

    public Double getPlanTotalByQuarter(Long planId, int quarter) {
        List<PlanItem> planItems = planItemRepository.findByPlanId(planId);

        return switch (quarter) {
            case 1 -> planItems.stream().mapToDouble(PlanItem::getQ1Plan).sum();
            case 2 -> planItems.stream().mapToDouble(PlanItem::getQ2Plan).sum();
            case 3 -> planItems.stream().mapToDouble(PlanItem::getQ3Plan).sum();
            case 4 -> planItems.stream().mapToDouble(PlanItem::getQ4Plan).sum();
            default -> throw new IllegalArgumentException("Invalid quarter: " + quarter);
        };
    }

    public Map<Long, Double> getQuarterlyPlanForBranch(Long branchId, Integer year, int quarter) {
        Plan plan = planRepository.findByBranchIdAndYear(branchId, year)
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found"));

        List<PlanItem> planItems = planItemRepository.findByPlanId(plan.getId());

        return planItems.stream()
                .collect(Collectors.toMap(
                        item -> item.getWorkCategory().getId(),
                        item -> switch (quarter) {
                            case 1 -> item.getQ1Plan();
                            case 2 -> item.getQ2Plan();
                            case 3 -> item.getQ3Plan();
                            case 4 -> item.getQ4Plan();
                            default -> 0.0;
                        }
                ));
    }

    public boolean isPlanEditable(Long planId) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found"));

        // План можно редактировать только если он в статусе DRAFT
        return plan.getStatus() == Plan.PlanStatus.DRAFT;
    }

    @Transactional
    public void approvePlan(Long planId) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found"));

        if (plan.getStatus() != Plan.PlanStatus.DRAFT) {
            throw new RuntimeException("Only draft plans can be approved");
        }

        plan.setStatus(Plan.PlanStatus.APPROVED);
        planRepository.save(plan);
    }

    @Transactional
    public void rejectPlan(Long planId) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found"));

        plan.setStatus(Plan.PlanStatus.DRAFT);
        planRepository.save(plan);
    }

    public List<Plan> findPlansByStatus(Plan.PlanStatus status) {
        return planRepository.findByStatus(status);
    }

    public List<Plan> findPlansByBranchAndStatus(Long branchId, Plan.PlanStatus status) {
        return planRepository.findByBranchIdAndStatus(branchId, status);
    }

    @Transactional
    public void bulkUpdatePlanStatus(List<Long> planIds, Plan.PlanStatus status) {
        List<Plan> plans = planRepository.findAllById(planIds);
        for (Plan plan : plans) {
            plan.setStatus(status);
        }
        planRepository.saveAll(plans);
    }
}