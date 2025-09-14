package by.iba.repair_report.service.impl;

import by.iba.repair_report.dto.request.EnterpriseRequest;
import by.iba.repair_report.dto.response.EnterpriseResponse;
import by.iba.repair_report.entity.Association;
import by.iba.repair_report.entity.Enterprise;
import by.iba.repair_report.exception.ResourceNotFoundException;
import by.iba.repair_report.repository.AssociationRepository;
import by.iba.repair_report.repository.BranchRepository;
import by.iba.repair_report.repository.EnterpriseRepository;
import by.iba.repair_report.service.EnterpriseService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnterpriseServiceImpl implements EnterpriseService {

    private final EnterpriseRepository enterpriseRepository;
    private final AssociationRepository associationRepository;
    private final BranchRepository branchRepository;

    public EnterpriseServiceImpl(EnterpriseRepository enterpriseRepository,
                                 AssociationRepository associationRepository,
                                 BranchRepository branchRepository) {
        this.enterpriseRepository = enterpriseRepository;
        this.associationRepository = associationRepository;
        this.branchRepository = branchRepository;
    }

    @Override
    @Transactional
    public EnterpriseResponse createEnterprise(EnterpriseRequest request) {
        Association association = associationRepository.findById(request.getAssociationId())
                .orElseThrow(() -> new ResourceNotFoundException("Association not found"));

        if (enterpriseRepository.existsByNameAndAssociationId(request.getName(), request.getAssociationId())) {
            throw new RuntimeException("Enterprise with this name already exists in this association");
        }

        Enterprise enterprise = new Enterprise(request.getName(), association);
        Enterprise savedEnterprise = enterpriseRepository.save(enterprise);
        return convertToResponse(savedEnterprise);
    }

    @Override
    @Transactional
    public EnterpriseResponse updateEnterprise(Long id, EnterpriseRequest request) {
        Enterprise enterprise = enterpriseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enterprise not found"));

        Association association = associationRepository.findById(request.getAssociationId())
                .orElseThrow(() -> new ResourceNotFoundException("Association not found"));

        if (!enterprise.getName().equals(request.getName()) &&
                enterpriseRepository.existsByNameAndAssociationIdAndIdNot(
                        request.getName(), request.getAssociationId(), id)) {
            throw new RuntimeException("Enterprise with this name already exists in this association");
        }

        enterprise.setName(request.getName());
        enterprise.setAssociation(association);

        Enterprise updatedEnterprise = enterpriseRepository.save(enterprise);
        return convertToResponse(updatedEnterprise);
    }

    @Override
    @Transactional
    public void deleteEnterprise(Long id) {
        Enterprise enterprise = enterpriseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enterprise not found"));

        if (branchRepository.countByEnterpriseId(id) > 0) {
            throw new RuntimeException("Cannot delete enterprise with existing branches");
        }

        enterpriseRepository.delete(enterprise);
    }

    @Override
    public EnterpriseResponse getEnterpriseById(Long id) {
        Enterprise enterprise = enterpriseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enterprise not found"));
        return convertToResponse(enterprise);
    }

    @Override
    public List<EnterpriseResponse> getEnterprisesByAssociation(Long associationId) {
        return enterpriseRepository.findByAssociationId(associationId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EnterpriseResponse> getAllEnterprises() {
        return enterpriseRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EnterpriseResponse> searchEnterprises(Long associationId, String name) {
        return enterpriseRepository.findByAssociationIdAndNameContaining(associationId, name).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private EnterpriseResponse convertToResponse(Enterprise enterprise) {
        EnterpriseResponse response = new EnterpriseResponse();
        response.setId(enterprise.getId());
        response.setName(enterprise.getName());
        response.setAssociationId(enterprise.getAssociation().getId());
        response.setAssociationName(enterprise.getAssociation().getName());
        response.setBranchCount(branchRepository.countByEnterpriseId(enterprise.getId()));
        return response;
    }
}
