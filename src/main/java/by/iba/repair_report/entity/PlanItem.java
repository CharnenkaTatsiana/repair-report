package by.iba.repair_report.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "plan_items")
public class PlanItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_category_id", nullable = false)
    private WorkCategory workCategory;

    // Плановые значения по кварталам
    private Double q1Plan;
    private Double q2Plan;
    private Double q3Plan;
    private Double q4Plan;

    // Годовой план можно не хранить, а вычислять (q1Plan + q2Plan + q3Plan + q4Plan)
    // Но для производительности часто хранят и вычисляемое поле.
    private Double annualPlan;

    // Конструкторы, геттеры, сеттеры
    public PlanItem() {}
    public PlanItem(Plan plan, WorkCategory workCategory) {
        this.plan = plan;
        this.workCategory = workCategory;
    }
    // ... getters and setters ...


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Plan getPlan() {
        return plan;
    }
    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public WorkCategory getWorkCategory() {
        return workCategory;
    }
    public void setWorkCategory(WorkCategory workCategory) {
        this.workCategory = workCategory;
    }

    public Double getQ1Plan() {
        return q1Plan;
    }
    public void setQ1Plan(Double q1Plan) {
        this.q1Plan = q1Plan;
    }

    public Double getQ2Plan() {
        return q2Plan;
    }
    public void setQ2Plan(Double q2Plan) {
        this.q2Plan = q2Plan;
    }

    public Double getQ3Plan() {
        return q3Plan;
    }
    public void setQ3Plan(Double q3Plan) {
        this.q3Plan = q3Plan;
    }

    public Double getQ4Plan() {
        return q4Plan;
    }
    public void setQ4Plan(Double q4Plan) {
        this.q4Plan = q4Plan;
    }

    public Double getAnnualPlan() {
        return annualPlan;
    }
    public void setAnnualPlan(Double annualPlan) {
        this.annualPlan = annualPlan;
    }

    // Метод для пересчета годового плана
    @PrePersist
    @PreUpdate
    public void calculateAnnualPlan() {
        this.annualPlan = (q1Plan != null ? q1Plan : 0.0) +
                (q2Plan != null ? q2Plan : 0.0) +
                (q3Plan != null ? q3Plan : 0.0) +
                (q4Plan != null ? q4Plan : 0.0);
    }
}
