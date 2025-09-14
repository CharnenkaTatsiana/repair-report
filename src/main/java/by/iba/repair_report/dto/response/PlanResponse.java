package by.iba.repair_report.dto.response;

import java.util.List;

public class PlanResponse {
    private Long id;
    private Long branchId;
    private String branchName;
    private Integer year;
    private String status;
    private List<PlanItemResponse> planItems;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getBranchId() { return branchId; }
    public void setBranchId(Long branchId) { this.branchId = branchId; }
    public String getBranchName() { return branchName; }
    public void setBranchName(String branchName) { this.branchName = branchName; }
    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public List<PlanItemResponse> getPlanItems() { return planItems; }
    public void setPlanItems(List<PlanItemResponse> planItems) { this.planItems = planItems; }
}
