package by.iba.repair_report.service;

import by.iba.repair_report.dto.request.AssociationRequest;
import by.iba.repair_report.dto.response.AssociationResponse;

import java.util.List;

public interface AssociationService {
    AssociationResponse createAssociation(AssociationRequest request);
    AssociationResponse updateAssociation(Long id, AssociationRequest request);
    void deleteAssociation(Long id);
    AssociationResponse getAssociationById(Long id);
    List<AssociationResponse> getAllAssociations();
    List<AssociationResponse> searchAssociations(String name);
}
