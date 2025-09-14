package by.iba.repair_report.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class BranchRequest {
    @NotBlank(message = "Branch name is required")
    @Size(max = 255, message = "Branch name must be less than 255 characters")
    private String name;

    @NotNull(message = "Enterprise ID is required")
    private Long enterpriseId;

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Long getEnterpriseId() { return enterpriseId; }
    public void setEnterpriseId(Long enterpriseId) { this.enterpriseId = enterpriseId; }
}
