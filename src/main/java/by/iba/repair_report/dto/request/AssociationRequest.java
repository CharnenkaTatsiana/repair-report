package by.iba.repair_report.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AssociationRequest {
    @NotBlank(message = "Association name is required")
    @Size(max = 255, message = "Association name must be less than 255 characters")
    private String name;

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
