package by.iba.repair_report.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "report_items")
public class ReportItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id", nullable = false)
    private Report report;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_category_id", nullable = false)
    private WorkCategory workCategory;

    // Эти данные БЕРУТСЯ из актуального плана на год для этого филиала
    private Double annualPlan;
    private Double quarterlyPlan; // План на текущий квартал (Q1, Q2, Q3, Q4)

    // Это поле ЗАПОЛНЯЕТ инженер филиала
    private Double actual; // Факт за текущий месяц

    // Конструкторы, геттеры, сеттеры
    public ReportItem() {}
    public ReportItem(Report report, WorkCategory workCategory, Double annualPlan, Double quarterlyPlan) {
        this.report = report;
        this.workCategory = workCategory;
        this.annualPlan = annualPlan;
        this.quarterlyPlan = quarterlyPlan;
    }
    // ... getters and setters ...

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Report getReport() {
        return report;
    }
    public void setReport(Report report) {
        this.report = report;
    }
    public WorkCategory getWorkCategory() {
        return workCategory;
    }
    public void setWorkCategory(WorkCategory workCategory) {
        this.workCategory = workCategory;
    }
    public Double getAnnualPlan() {
        return annualPlan;
    }
    public void setAnnualPlan(Double annualPlan) {
        this.annualPlan = annualPlan;
    }
    public Double getQuarterlyPlan() {
        return quarterlyPlan;
    }
    public void setQuarterlyPlan(Double quarterlyPlan) {
        this.quarterlyPlan = quarterlyPlan;
    }
    public Double getActual() {
        return actual;
    }
    public void setActual(Double actual) {
        this.actual = actual;
    }

}
