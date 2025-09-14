package by.iba.repair_report.service;

import by.iba.repair_report.dto.request.BranchRequest;
import by.iba.repair_report.dto.response.BranchResponse;

import java.util.List;

public interface BranchService {
    BranchResponse createBranch(BranchRequest request);
    BranchResponse updateBranch(Long id, BranchRequest request);
    void deleteBranch(Long id);
    BranchResponse getBranchById(Long id);
    List<BranchResponse> getBranchesByEnterprise(Long enterpriseId);
    List<BranchResponse> getBranchesByAssociation(Long associationId);
    List<BranchResponse> getAllBranches();
    List<BranchResponse> searchBranches(Long enterpriseId, String name);
}
