package by.iba.repair_report.service.impl;

import by.iba.repair_report.dto.request.BranchRequest;
import by.iba.repair_report.dto.response.BranchResponse;
import by.iba.repair_report.entity.Branch;
import by.iba.repair_report.entity.Enterprise;
import by.iba.repair_report.exception.ResourceNotFoundException;
import by.iba.repair_report.repository.BranchRepository;
import by.iba.repair_report.repository.EnterpriseRepository;
import by.iba.repair_report.service.BranchService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BranchServiceImpl implements BranchService {

    private final BranchRepository branchRepository;
    private final EnterpriseRepository enterpriseRepository;

    public BranchServiceImpl(BranchRepository branchRepository,
                             EnterpriseRepository enterpriseRepository) {
        this.branchRepository = branchRepository;
        this.enterpriseRepository = enterpriseRepository;
    }

    @Override
    @Transactional
    public BranchResponse createBranch(BranchRequest request) {
        Enterprise enterprise = enterpriseRepository.findById(request.getEnterpriseId())
                .orElseThrow(() -> new ResourceNotFoundException("Enterprise not found"));

        if (branchRepository.existsByNameAndEnterpriseId(request.getName(), request.getEnterpriseId())) {
            throw new RuntimeException("Branch with this name already exists in this enterprise");
        }

        Branch branch = new Branch(request.getName(), enterprise);
        Branch savedBranch = branchRepository.save(branch);
        return convertToResponse(savedBranch);
    }

    @Override
    @Transactional
    public BranchResponse updateBranch(Long id, BranchRequest request) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found"));

        Enterprise enterprise = enterpriseRepository.findById(request.getEnterpriseId())
                .orElseThrow(() -> new ResourceNotFoundException("Enterprise not found"));

        if (!branch.getName().equals(request.getName()) &&
                branchRepository.existsByNameAndEnterpriseIdAndIdNot(
                        request.getName(), request.getEnterpriseId(), id)) {
            throw new RuntimeException("Branch with this name already exists in this enterprise");
        }

        branch.setName(request.getName());
        branch.setEnterprise(enterprise);

        Branch updatedBranch = branchRepository.save(branch);
        return convertToResponse(updatedBranch);
    }

    @Override
    @Transactional
    public void deleteBranch(Long id) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found"));

        // Проверка на существующие планы и отчеты
        // Можно добавить дополнительные проверки здесь

        branchRepository.delete(branch);
    }

    @Override
    public BranchResponse getBranchById(Long id) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found"));
        return convertToResponse(branch);
    }

    @Override
    public List<BranchResponse> getBranchesByEnterprise(Long enterpriseId) {
        return branchRepository.findByEnterpriseId(enterpriseId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<BranchResponse> getBranchesByAssociation(Long associationId) {
        return branchRepository.findByAssociationId(associationId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<BranchResponse> getAllBranches() {
        return branchRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<BranchResponse> searchBranches(Long enterpriseId, String name) {
        return branchRepository.findByEnterpriseIdAndNameContaining(enterpriseId, name).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private BranchResponse convertToResponse(Branch branch) {
        BranchResponse response = new BranchResponse();
        response.setId(branch.getId());
        response.setName(branch.getName());
        response.setEnterpriseId(branch.getEnterprise().getId());
        response.setEnterpriseName(branch.getEnterprise().getName());
        response.setAssociationId(branch.getEnterprise().getAssociation().getId());
        response.setAssociationName(branch.getEnterprise().getAssociation().getName());
        return response;
    }
}
