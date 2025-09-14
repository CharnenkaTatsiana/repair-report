package by.iba.repair_report.dto.response;

import java.time.YearMonth;
import java.util.List;

public class ReportResponse {
    private Long id;
    private Long branchId;
    private String branchName;
    private YearMonth period;
    private Integer quarter;
    private String status;
    private List<ReportItemResponse> reportItems;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getBranchId() { return branchId; }
    public void setBranchId(Long branchId) { this.branchId = branchId; }
    public String getBranchName() { return branchName; }
    public void setBranchName(String branchName) { this.branchName = branchName; }
    public YearMonth getPeriod() { return period; }
    public void setPeriod(YearMonth period) { this.period = period; }
    public Integer getQuarter() { return quarter; }
    public void setQuarter(Integer quarter) { this.quarter = quarter; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public List<ReportItemResponse> getReportItems() { return reportItems; }
    public void setReportItems(List<ReportItemResponse> reportItems) { this.reportItems = reportItems; }
}