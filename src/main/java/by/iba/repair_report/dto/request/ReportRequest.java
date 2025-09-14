package by.iba.repair_report.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.YearMonth;
import java.util.Map;

public class ReportRequest {
    @NotNull(message = "Branch ID is required")
    private Long branchId;

    @NotNull(message = "Period is required")
    private YearMonth period;

    private Map<Long, Double> reportItems; // workCategoryId -> actual value

    // Getters and Setters
    public Long getBranchId() { return branchId; }
    public void setBranchId(Long branchId) { this.branchId = branchId; }
    public YearMonth getPeriod() { return period; }
    public void setPeriod(YearMonth period) { this.period = period; }
    public Map<Long, Double> getReportItems() { return reportItems; }
    public void setReportItems(Map<Long, Double> reportItems) { this.reportItems = reportItems; }
}
