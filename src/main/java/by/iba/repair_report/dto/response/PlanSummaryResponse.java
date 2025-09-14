package by.iba.repair_report.dto.response;

import java.util.Map;

public class PlanSummaryResponse {
    private Long branchId;
    private String branchName;
    private Integer year;
    private Map<String, Double> totalByNetworkType; // MAIN -> total, DISTRIBUTION -> total
    private Double grandTotal;

    // Getters and Setters
    public Long getBranchId() { return branchId; }
    public void setBranchId(Long branchId) { this.branchId = branchId; }
    public String getBranchName() { return branchName; }
    public void setBranchName(String branchName) { this.branchName = branchName; }
    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }
    public Map<String, Double> getTotalByNetworkType() { return totalByNetworkType; }
    public void setTotalByNetworkType(Map<String, Double> totalByNetworkType) { this.totalByNetworkType = totalByNetworkType; }
    public Double getGrandTotal() { return grandTotal; }
    public void setGrandTotal(Double grandTotal) { this.grandTotal = grandTotal; }
}
