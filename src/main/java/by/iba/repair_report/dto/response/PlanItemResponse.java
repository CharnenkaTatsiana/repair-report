package by.iba.repair_report.dto.response;

public class PlanItemResponse {
    private Long workCategoryId;
    private String workName;
    private String networkType;
    private Double q1;
    private Double q2;
    private Double q3;
    private Double q4;
    private Double annual;

    // Getters and Setters
    public Long getWorkCategoryId() { return workCategoryId; }
    public void setWorkCategoryId(Long workCategoryId) { this.workCategoryId = workCategoryId; }
    public String getWorkName() { return workName; }
    public void setWorkName(String workName) { this.workName = workName; }
    public String getNetworkType() { return networkType; }
    public void setNetworkType(String networkType) { this.networkType = networkType; }
    public Double getQ1() { return q1; }
    public void setQ1(Double q1) { this.q1 = q1; }
    public Double getQ2() { return q2; }
    public void setQ2(Double q2) { this.q2 = q2; }
    public Double getQ3() { return q3; }
    public void setQ3(Double q3) { this.q3 = q3; }
    public Double getQ4() { return q4; }
    public void setQ4(Double q4) { this.q4 = q4; }
    public Double getAnnual() { return annual; }
    public void setAnnual(Double annual) { this.annual = annual; }
}
