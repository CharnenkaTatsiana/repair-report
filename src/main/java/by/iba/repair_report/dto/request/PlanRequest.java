package by.iba.repair_report.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.Map;

public class PlanRequest {
    @NotNull(message = "Branch ID is required")
    @Positive(message = "Branch ID must be positive")
    private Long branchId;

    @NotNull(message = "Year is required")
    private Integer year;

    private Map<Long, QuarterValues> planItems;

    // Вложенный статический класс
    public static class QuarterValues {
        private Double q1;
        private Double q2;
        private Double q3;
        private Double q4;

        // Геттеры и сеттеры
        public Double getQ1() { return q1; }
        public void setQ1(Double q1) { this.q1 = q1; }

        public Double getQ2() { return q2; }
        public void setQ2(Double q2) { this.q2 = q2; }

        public Double getQ3() { return q3; }
        public void setQ3(Double q3) { this.q3 = q3; }

        public Double getQ4() { return q4; }
        public void setQ4(Double q4) { this.q4 = q4; }
    }

    // Геттеры и сеттеры для PlanRequest
    public Long getBranchId() { return branchId; }
    public void setBranchId(Long branchId) { this.branchId = branchId; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public Map<Long, QuarterValues> getPlanItems() { return planItems; }
    public void setPlanItems(Map<Long, QuarterValues> planItems) { this.planItems = planItems; }
}