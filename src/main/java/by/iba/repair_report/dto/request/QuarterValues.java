package by.iba.repair_report.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class QuarterValues {
    @NotNull(message = "Q1 value is required")
    @DecimalMin(value = "0.0", message = "Q1 value cannot be negative")
    private Double q1;

    @NotNull(message = "Q2 value is required")
    @DecimalMin(value = "0.0", message = "Q2 value cannot be negative")
    private Double q2;

    @NotNull(message = "Q3 value is required")
    @DecimalMin(value = "0.0", message = "Q3 value cannot be negative")
    private Double q3;

    @NotNull(message = "Q4 value is required")
    @DecimalMin(value = "0.0", message = "Q4 value cannot be negative")
    private Double q4;

    // Getters and Setters
    public Double getQ1() { return q1; }
    public void setQ1(Double q1) { this.q1 = q1; }
    public Double getQ2() { return q2; }
    public void setQ2(Double q2) { this.q2 = q2; }
    public Double getQ3() { return q3; }
    public void setQ3(Double q3) { this.q3 = q3; }
    public Double getQ4() { return q4; }
    public void setQ4(Double q4) { this.q4 = q4; }
}
