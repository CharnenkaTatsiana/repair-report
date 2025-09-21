package by.iba.repair_report.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

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
    private BigDecimal q1Plan;
    private BigDecimal q2Plan;
    private BigDecimal q3Plan;
    private BigDecimal q4Plan;

    // Годовой план можно не хранить, а вычислять (q1Plan + q2Plan + q3Plan + q4Plan)
    // Но для производительности часто хранят и вычисляемое поле.
    private BigDecimal annualPlan;

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

    public BigDecimal getQ1Plan() {
        return q1Plan;
    }
    public void setQ1Plan(BigDecimal q1Plan) {
        this.q1Plan = q1Plan;
    }

    public BigDecimal getQ2Plan() {
        return q2Plan;
    }
    public void setQ2Plan(BigDecimal q2Plan) {
        this.q2Plan = q2Plan;
    }

    public BigDecimal getQ3Plan() {
        return q3Plan;
    }
    public void setQ3Plan(BigDecimal q3Plan) {
        this.q3Plan = q3Plan;
    }

    public BigDecimal getQ4Plan() {
        return q4Plan;
    }
    public void setQ4Plan(BigDecimal q4Plan) {
        this.q4Plan = q4Plan;
    }

    public BigDecimal getAnnualPlan() {
        return annualPlan;
    }
    public void setAnnualPlan(BigDecimal annualPlan) {
        this.annualPlan = annualPlan;
    }

    // Метод для пересчета годового плана
    @PrePersist
    @PreUpdate
    public void calculateAnnualPlan() {
        BigDecimal q1Val = q1Plan != null ? q1Plan : BigDecimal.ZERO;
        BigDecimal q2Val = q2Plan != null ? q2Plan : BigDecimal.ZERO;
        BigDecimal q3Val = q3Plan != null ? q3Plan : BigDecimal.ZERO;
        BigDecimal q4Val = q4Plan != null ? q4Plan : BigDecimal.ZERO;

        this.annualPlan = q1Val.add(q2Val).add(q3Val).add(q4Val);
    }
}
