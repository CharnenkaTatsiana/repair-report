package by.iba.repair_report.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class EnterpriseRequest {
    @NotBlank(message = "Enterprise name is required")
    @Size(max = 255, message = "Enterprise name must be less than 255 characters")
    private String name;

    @NotNull(message = "Association ID is required")
    private Long associationId;

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Long getAssociationId() { return associationId; }
    public void setAssociationId(Long associationId) { this.associationId = associationId; }
}
