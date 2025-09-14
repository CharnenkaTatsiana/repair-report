package by.iba.repair_report.dto.response;

import java.time.YearMonth;
import java.util.Map;

public class ReportSummaryResponse {
    private Long branchId;
    private String branchName;
    private YearMonth period;
    private Integer quarter;
    private Map<String, Double> plannedByNetworkType;
    private Map<String, Double> actualByNetworkType;
    private Map<String, Double> completionByNetworkType;
    private Double totalPlanned;
    private Double totalActual;
    private Double totalCompletion;

    // Getters and Setters
    public Long getBranchId() { return branchId; }
    public void setBranchId(Long branchId) { this.branchId = branchId; }
    public String getBranchName() { return branchName; }
    public void setBranchName(String branchName) { this.branchName = branchName; }
    public YearMonth getPeriod() { return period; }
    public void setPeriod(YearMonth period) { this.period = period; }
    public Integer getQuarter() { return quarter; }
    public void setQuarter(Integer quarter) { this.quarter = quarter; }
    public Map<String, Double> getPlannedByNetworkType() { return plannedByNetworkType; }
    public void setPlannedByNetworkType(Map<String, Double> plannedByNetworkType) { this.plannedByNetworkType = plannedByNetworkType; }
    public Map<String, Double> getActualByNetworkType() { return actualByNetworkType; }
    public void setActualByNetworkType(Map<String, Double> actualByNetworkType) { this.actualByNetworkType = actualByNetworkType; }
    public Map<String, Double> getCompletionByNetworkType() { return completionByNetworkType; }
    public void setCompletionByNetworkType(Map<String, Double> completionByNetworkType) { this.completionByNetworkType = completionByNetworkType; }
    public Double getTotalPlanned() { return totalPlanned; }
    public void setTotalPlanned(Double totalPlanned) { this.totalPlanned = totalPlanned; }
    public Double getTotalActual() { return totalActual; }
    public void setTotalActual(Double totalActual) { this.totalActual = totalActual; }
    public Double getTotalCompletion() { return totalCompletion; }
    public void setTotalCompletion(Double totalCompletion) { this.totalCompletion = totalCompletion; }
}
