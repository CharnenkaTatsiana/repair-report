package by.iba.repair_report.dto.response;

public class AssociationResponse {
    private Long id;
    private String name;
    private Integer enterpriseCount;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getEnterpriseCount() { return enterpriseCount; }
    public void setEnterpriseCount(Integer enterpriseCount) { this.enterpriseCount = enterpriseCount; }
}
