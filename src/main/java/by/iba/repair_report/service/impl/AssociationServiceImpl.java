package by.iba.repair_report.service.impl;

import by.iba.repair_report.dto.request.AssociationRequest;
import by.iba.repair_report.dto.response.AssociationResponse;
import by.iba.repair_report.entity.Association;
import by.iba.repair_report.exception.ResourceNotFoundException;
import by.iba.repair_report.repository.AssociationRepository;
import by.iba.repair_report.repository.EnterpriseRepository;
import by.iba.repair_report.service.AssociationService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssociationServiceImpl implements AssociationService {

    private final AssociationRepository associationRepository;
    private final EnterpriseRepository enterpriseRepository;

    public AssociationServiceImpl(AssociationRepository associationRepository,
                                  EnterpriseRepository enterpriseRepository) {
        this.associationRepository = associationRepository;
        this.enterpriseRepository = enterpriseRepository;
    }

    @Override
    @Transactional
    public AssociationResponse createAssociation(AssociationRequest request) {
        if (associationRepository.existsByName(request.getName())) {
            throw new RuntimeException("Association with this name already exists");
        }

        Association association = new Association(request.getName());
        Association savedAssociation = associationRepository.save(association);
        return convertToResponse(savedAssociation);
    }

    @Override
    @Transactional
    public AssociationResponse updateAssociation(Long id, AssociationRequest request) {
        Association association = associationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Association not found"));

        if (!association.getName().equals(request.getName()) &&
                associationRepository.existsByNameAndIdNot(request.getName(), id)) {
            throw new RuntimeException("Association with this name already exists");
        }

        association.setName(request.getName());
        Association updatedAssociation = associationRepository.save(association);
        return convertToResponse(updatedAssociation);
    }

    @Override
    @Transactional
    public void deleteAssociation(Long id) {
        Association association = associationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Association not found"));

        if (enterpriseRepository.countByAssociationId(id) > 0) {
            throw new RuntimeException("Cannot delete association with existing enterprises");
        }

        associationRepository.delete(association);
    }

    @Override
    public AssociationResponse getAssociationById(Long id) {
        Association association = associationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Association not found"));
        return convertToResponse(association);
    }

    @Override
    public List<AssociationResponse> getAllAssociations() {
        return associationRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AssociationResponse> searchAssociations(String name) {
        return associationRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private AssociationResponse convertToResponse(Association association) {
        AssociationResponse response = new AssociationResponse();
        response.setId(association.getId());
        response.setName(association.getName());
        response.setEnterpriseCount(enterpriseRepository.countByAssociationId(association.getId()));
        return response;
    }
}
