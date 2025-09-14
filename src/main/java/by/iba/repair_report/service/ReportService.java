package by.iba.repair_report.service;

import by.iba.repair_report.dto.request.ReportRequest;
import by.iba.repair_report.dto.request.UpdateReportStatusRequest;
import by.iba.repair_report.dto.response.ReportResponse;
import by.iba.repair_report.dto.response.ReportSummaryResponse;

import java.time.YearMonth;
import java.util.List;

public interface ReportService {
    ReportResponse createReport(ReportRequest request);
    ReportResponse updateReport(Long reportId, ReportRequest request);
    ReportResponse updateReportStatus(Long reportId, UpdateReportStatusRequest request);
    void deleteReport(Long reportId);
    ReportResponse getReportById(Long reportId);
    List<ReportResponse> getReportsByBranch(Long branchId);
    List<ReportResponse> getReportsByBranchAndYear(Long branchId, Integer year);
    List<ReportResponse> getReportsByEnterprise(Long enterpriseId);
    List<ReportResponse> getReportsByAssociation(Long associationId);
    ReportResponse getReportByBranchAndPeriod(Long branchId, YearMonth period);
    ReportSummaryResponse getReportSummary(Long reportId);
    boolean existsReportForBranchAndPeriod(Long branchId, YearMonth period);
}
