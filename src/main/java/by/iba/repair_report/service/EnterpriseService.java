package by.iba.repair_report.service;

import by.iba.repair_report.dto.request.EnterpriseRequest;
import by.iba.repair_report.dto.response.EnterpriseResponse;

import java.util.List;

public interface EnterpriseService {
    EnterpriseResponse createEnterprise(EnterpriseRequest request);
    EnterpriseResponse updateEnterprise(Long id, EnterpriseRequest request);
    void deleteEnterprise(Long id);
    EnterpriseResponse getEnterpriseById(Long id);
    List<EnterpriseResponse> getEnterprisesByAssociation(Long associationId);
    List<EnterpriseResponse> getAllEnterprises();
    List<EnterpriseResponse> searchEnterprises(Long associationId, String name);
}
