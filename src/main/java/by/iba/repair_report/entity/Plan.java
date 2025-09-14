package by.iba.repair_report.entity;

import jakarta.persistence.*;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "plans", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"branch_id", "year"})
})
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @Column(nullable = false)
    private Year year; // Год, на который составлен план

    // План состоит из множества записей (строк) по работам
    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlanItem> planItems = new ArrayList<>();

    // Статус плана (Черновик, Отправлен, Утвержден)
    @Enumerated(EnumType.STRING)
    private PlanStatus status = PlanStatus.DRAFT;

    public enum PlanStatus {
        DRAFT, SENT, APPROVED
    }

    // Конструкторы, геттеры, сеттеры
    public Plan() {}
    public Plan(Branch branch, Year year) {
        this.branch = branch;
        this.year = year;
    }
    // ... getters and setters ...

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Branch getBranch() {
        return branch;
    }
    public void setBranch(Branch branch) {
        this.branch = branch;
    }
    public Year getYear() {
        return year;
    }
    public void setYear(Year year) {
        this.year = year;
    }
    public List<PlanItem> getPlanItems() {
        return planItems;
    }
    public void setPlanItems(List<PlanItem> planItems) {
        this.planItems = planItems;
    }
    public PlanStatus getStatus() {
        return status;
    }
    public void setStatus(PlanStatus status) {
        this.status = status;
    }



}
