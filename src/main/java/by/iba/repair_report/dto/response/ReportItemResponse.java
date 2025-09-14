package by.iba.repair_report.dto.response;

public class ReportItemResponse {
    private Long workCategoryId;
    private String workName;
    private String networkType;
    private Double annualPlan;
    private Double quarterlyPlan;
    private Double actual;
    private Double completionPercentage;

    // Getters and Setters
    public Long getWorkCategoryId() { return workCategoryId; }
    public void setWorkCategoryId(Long workCategoryId) { this.workCategoryId = workCategoryId; }
    public String getWorkName() { return workName; }
    public void setWorkName(String workName) { this.workName = workName; }
    public String getNetworkType() { return networkType; }
    public void setNetworkType(String networkType) { this.networkType = networkType; }
    public Double getAnnualPlan() { return annualPlan; }
    public void setAnnualPlan(Double annualPlan) { this.annualPlan = annualPlan; }
    public Double getQuarterlyPlan() { return quarterlyPlan; }
    public void setQuarterlyPlan(Double quarterlyPlan) { this.quarterlyPlan = quarterlyPlan; }
    public Double getActual() { return actual; }
    public void setActual(Double actual) { this.actual = actual; }
    public Double getCompletionPercentage() { return completionPercentage; }
    public void setCompletionPercentage(Double completionPercentage) { this.completionPercentage = completionPercentage; }
}
