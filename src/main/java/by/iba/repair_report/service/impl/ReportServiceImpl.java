package by.iba.repair_report.service.impl;

import by.iba.repair_report.dto.request.ReportRequest;
import by.iba.repair_report.dto.request.UpdateReportStatusRequest;
import by.iba.repair_report.dto.response.ReportItemResponse;
import by.iba.repair_report.dto.response.ReportResponse;
import by.iba.repair_report.dto.response.ReportSummaryResponse;
import by.iba.repair_report.entity.*;
import by.iba.repair_report.exception.ResourceNotFoundException;
import by.iba.repair_report.repository.BranchRepository;
import by.iba.repair_report.repository.ReportRepository;
import by.iba.repair_report.repository.WorkCategoryRepository;
import by.iba.repair_report.service.PlanService;
import by.iba.repair_report.service.ReportService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final BranchRepository branchRepository;
    private final WorkCategoryRepository workCategoryRepository;
    private final PlanService planService;

    public ReportServiceImpl(ReportRepository reportRepository,
                             BranchRepository branchRepository,
                             WorkCategoryRepository workCategoryRepository,
                             PlanService planService) {
        this.reportRepository = reportRepository;
        this.branchRepository = branchRepository;
        this.workCategoryRepository = workCategoryRepository;
        this.planService = planService;
    }

    @Override
    @Transactional
    public ReportResponse createReport(ReportRequest request) {
        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id: " + request.getBranchId()));

        if (reportRepository.existsByBranchIdAndPeriod(request.getBranchId(), request.getPeriod())) {
            throw new RuntimeException("Report for branch " + branch.getName() + " and period " + request.getPeriod() + " already exists");
        }

        Report report = new Report();
        report.setBranch(branch);
        report.setPeriod(request.getPeriod());
        report.setStatus(Report.ReportStatus.DRAFT);

        Report savedReport = reportRepository.save(report);
        createReportItems(savedReport, request.getReportItems());

        return convertToResponse(savedReport);
    }

    private void createReportItems(Report report, Map<Long, Double> reportItems) {
        if (reportItems != null && !reportItems.isEmpty()) {
            int year = report.getPeriod().getYear();
            int quarter = report.getQuarter();

            for (Map.Entry<Long, Double> entry : reportItems.entrySet()) {
                Long workCategoryId = entry.getKey();
                Double actualValue = entry.getValue();

                WorkCategory workCategory = workCategoryRepository.findById(workCategoryId)
                        .orElseThrow(() -> new ResourceNotFoundException("Work category not found with id: " + workCategoryId));

                if (!workCategory.getIsActive()) {
                    throw new RuntimeException("Work category " + workCategory.getName() + " is not active");
                }

                // Получаем плановые значения
                Map<Long, java.math.BigDecimal> quarterlyPlan = planService.getQuarterlyPlanForBranch(
                        report.getBranch().getId(), year, quarter);

                java.math.BigDecimal annualPlan = planService.getPlanTotalByQuarter(
                        getPlanIdForBranchAndYear(report.getBranch().getId(), year), 0); // 0 для годового плана

                ReportItem reportItem = new ReportItem();
                reportItem.setReport(report);
                reportItem.setWorkCategory(workCategory);
                reportItem.setAnnualPlan(annualPlan != null ? annualPlan.doubleValue() : 0.0);
                reportItem.setQuarterlyPlan(quarterlyPlan.get(workCategoryId) != null ?
                        quarterlyPlan.get(workCategoryId).doubleValue() : 0.0);
                reportItem.setActual(actualValue);

                // Сохранение reportItem будет через cascade
            }
        }
    }

    private Long getPlanIdForBranchAndYear(Long branchId, Integer year) {
        // Реализуйте этот метод в PlanService или здесь
        return null; // Заглушка
    }

    @Override
    @Transactional
    public ReportResponse updateReport(Long reportId, ReportRequest request) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + reportId));

        if (!report.getBranch().getId().equals(request.getBranchId())) {
            Branch branch = branchRepository.findById(request.getBranchId())
                    .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id: " + request.getBranchId()));
            report.setBranch(branch);
        }

        if (!report.getPeriod().equals(request.getPeriod())) {
            if (reportRepository.existsByBranchIdAndPeriod(request.getBranchId(), request.getPeriod())) {
                throw new RuntimeException("Report for this branch and period already exists");
            }
            report.setPeriod(request.getPeriod());
        }

        // Очищаем старые items и создаем новые
        report.getReportItems().clear();
        createReportItems(report, request.getReportItems());

        Report updatedReport = reportRepository.save(report);
        return convertToResponse(updatedReport);
    }

    @Override
    @Transactional
    public ReportResponse updateReportStatus(Long reportId, UpdateReportStatusRequest request) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + reportId));

        report.setStatus(request.getStatus());
        Report updatedReport = reportRepository.save(report);
        return convertToResponse(updatedReport);
    }

    @Override
    @Transactional
    public void deleteReport(Long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + reportId));
        reportRepository.delete(report);
    }

    @Override
    public ReportResponse getReportById(Long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + reportId));
        return convertToResponse(report);
    }

    @Override
    public List<ReportResponse> getReportsByBranch(Long branchId) {
        if (!branchRepository.existsById(branchId)) {
            throw new ResourceNotFoundException("Branch not found with id: " + branchId);
        }

        return reportRepository.findByBranchId(branchId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReportResponse> getReportsByBranchAndYear(Long branchId, Integer year) {
        return reportRepository.findByBranchId(branchId).stream()
                .filter(report -> report.getPeriod().getYear() == year)
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReportResponse> getReportsByEnterprise(Long enterpriseId) {
        return reportRepository.findByEnterpriseId(enterpriseId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReportResponse> getReportsByAssociation(Long associationId) {
        return reportRepository.findByAssociationId(associationId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ReportResponse getReportByBranchAndPeriod(Long branchId, YearMonth period) {
        return reportRepository.findByBranchIdAndPeriod(branchId, period)
                .map(this::convertToResponse)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Report not found for branch id: " + branchId + " and period: " + period));
    }

    @Override
    public ReportSummaryResponse getReportSummary(Long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + reportId));

        ReportSummaryResponse summary = new ReportSummaryResponse();
        summary.setBranchId(report.getBranch().getId());
        summary.setBranchName(report.getBranch().getName());
        summary.setPeriod(report.getPeriod());
        summary.setQuarter(report.getQuarter());

        // TODO: Реализовать расчет суммарных значений
        return summary;
    }

    @Override
    public boolean existsReportForBranchAndPeriod(Long branchId, YearMonth period) {
        return reportRepository.existsByBranchIdAndPeriod(branchId, period);
    }

    private ReportResponse convertToResponse(Report report) {
        ReportResponse response = new ReportResponse();
        response.setId(report.getId());
        response.setBranchId(report.getBranch().getId());
        response.setBranchName(report.getBranch().getName());
        response.setPeriod(report.getPeriod());
        response.setQuarter(report.getQuarter());
        response.setStatus(report.getStatus().name());

        List<ReportItemResponse> itemResponses = report.getReportItems().stream()
                .map(this::convertToItemResponse)
                .collect(Collectors.toList());

        response.setReportItems(itemResponses);
        return response;
    }

    private ReportItemResponse convertToItemResponse(ReportItem reportItem) {
        ReportItemResponse response = new ReportItemResponse();
        response.setWorkCategoryId(reportItem.getWorkCategory().getId());
        response.setWorkName(reportItem.getWorkCategory().getName());
        response.setNetworkType(reportItem.getWorkCategory().getNetworkType().name());
        response.setAnnualPlan(reportItem.getAnnualPlan());
        response.setQuarterlyPlan(reportItem.getQuarterlyPlan());
        response.setActual(reportItem.getActual());

        if (reportItem.getQuarterlyPlan() > 0) {
            response.setCompletionPercentage((reportItem.getActual() / reportItem.getQuarterlyPlan()) * 100);
        } else {
            response.setCompletionPercentage(0.0);
        }

        return response;
    }
}