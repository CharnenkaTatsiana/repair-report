package by.iba.repair_report.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "enterprises")
public class Enterprise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 255)
    private String name;

    // Предприятие принадлежит объединению
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "association_id", nullable = false)
    private Association association;

    // Предприятие имеет много филиалов
    @OneToMany(mappedBy = "enterprise", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Branch> branches = new ArrayList<>();

    // Конструкторы, геттеры, сеттеры
    public Enterprise() {}
    public Enterprise(String name, Association association) {
        this.name = name;
        this.association = association;
    }
    // ... getters and setters ...

    public Long getId() {
        return id;
    }
    public void setId(Long id) {}
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Association getAssociation() {
        return association;
    }
    public void setAssociation(Association association) {
        this.association = association;
    }
    public List<Branch> getBranches() {
        return branches;
    }
    public void setBranches(List<Branch> branches) {
        this.branches = branches;
    }

}
