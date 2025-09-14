package by.iba.repair_report.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "associations")
public class Association {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 255)
    private String name;

    // Объединение имеет много предприятий
    @OneToMany(mappedBy = "association", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Enterprise> enterprises = new ArrayList<>();

    // Конструкторы, геттеры, сеттеры
    public Association() {}
    public Association(String name) { this.name = name; }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName (String name) {
        this.name = name;
    }
    public List<Enterprise> getEnterprises() {
        return enterprises;
    }
    public void setEnterprises(List<Enterprise> enterprises) {
        this.enterprises = enterprises;
    }
}