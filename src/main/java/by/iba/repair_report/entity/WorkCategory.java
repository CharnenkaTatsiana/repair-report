package by.iba.repair_report.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "work_categories")
public class WorkCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 500)
    private String name; // Например, "Ремонт ВЛ 35 кВ"

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private NetworkType networkType; // "MAIN" или "DISTRIBUTION"

    // Флаг активности. Инженер объединения может "удалять" работы,
    // устанавливая isActive = false, чтобы не нарушать старые отчеты.
    private Boolean isActive = true;

    public enum NetworkType {
        MAIN,           // Основная сеть
        DISTRIBUTION    // Распределительная сеть
    }

    // Конструкторы, геттеры, сеттеры
    public WorkCategory() {}
    public WorkCategory(String name, NetworkType networkType) {
        this.name = name;
        this.networkType = networkType;
    }
    // ... getters and setters ...

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public NetworkType getNetworkType() {
        return networkType;
    }
    public void setNetworkType(NetworkType networkType) {
        this.networkType = networkType;
    }
    public Boolean getIsActive() {
        return isActive;
    }
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

}

