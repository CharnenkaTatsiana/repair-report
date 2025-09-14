package by.iba.repair_report.dto.request;

import by.iba.repair_report.entity.WorkCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class WorkCategoryRequest {
    @NotBlank(message = "Work name is required")
    @Size(max = 500, message = "Work name must be less than 500 characters")
    private String name;

    @NotNull(message = "Network type is required")
    private WorkCategory.NetworkType networkType;

    private Boolean isActive = true;

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public WorkCategory.NetworkType getNetworkType() { return networkType; }
    public void setNetworkType(WorkCategory.NetworkType networkType) { this.networkType = networkType; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}
