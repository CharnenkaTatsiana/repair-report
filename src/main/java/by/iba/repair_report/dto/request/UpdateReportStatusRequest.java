package by.iba.repair_report.dto.request;

import by.iba.repair_report.entity.Report;
import jakarta.validation.constraints.NotNull;

public class UpdateReportStatusRequest {
    @NotNull(message = "Status is required")
    private Report.ReportStatus status;

    // Getters and Setters
    public Report.ReportStatus getStatus() { return status; }
    public void setStatus(Report.ReportStatus status) { this.status = status; }
}
