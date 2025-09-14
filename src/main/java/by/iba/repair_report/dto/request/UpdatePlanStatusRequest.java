package by.iba.repair_report.dto.request;

import by.iba.repair_report.entity.Plan;
import jakarta.validation.constraints.NotNull;

public class UpdatePlanStatusRequest {
    @NotNull(message = "Status is required")
    private Plan.PlanStatus status;

    // Getters and Setters
    public Plan.PlanStatus getStatus() { return status; }
    public void setStatus(Plan.PlanStatus status) { this.status = status; }
}
