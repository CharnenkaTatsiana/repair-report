package by.iba.repair_report.entity;

import jakarta.persistence.*;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reports", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"branch_id", "period"})
})
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    // Период отчета (Год-Месяц). Например, 2023-04
    @Column(nullable = false)
    private YearMonth period;

    // Отчет состоит из множества записей (строк) по работам
    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReportItem> reportItems = new ArrayList<>();

    // Статус отчета (Черновик, Отправлен)
    @Enumerated(EnumType.STRING)
    private ReportStatus status = ReportStatus.DRAFT;

    public enum ReportStatus {
        DRAFT, SENT
    }

    // Конструкторы, геттеры, сеттеры
    public Report() {}
    public Report(Branch branch, YearMonth period) {
        this.branch = branch;
        this.period = period;
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
    public YearMonth getPeriod() {
        return period;
    }
    public void setPeriod(YearMonth period) {
        this.period = period;
    }
    public List<ReportItem> getReportItems() {
        return reportItems;
    }
    public void setReportItems(List<ReportItem> reportItems) {
        this.reportItems = reportItems;
    }
    public ReportStatus getStatus() {
        return status;
    }
    public void setStatus(ReportStatus status) {
        this.status = status;
    }


    // Вспомогательный метод для получения квартала из периода
    public int getQuarter() {
        int month = period.getMonthValue();
        return (month - 1) / 3 + 1;
    }
}
