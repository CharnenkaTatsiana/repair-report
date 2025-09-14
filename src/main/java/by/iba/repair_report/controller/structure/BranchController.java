package by.iba.repair_report.controller.structure;

import by.iba.repair_report.dto.request.BranchRequest;
import by.iba.repair_report.dto.response.BranchResponse;
import by.iba.repair_report.service.BranchService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/branches")
public class BranchController {

    private final BranchService branchService;

    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('ASSOCIATION_ENG') or hasRole('ENTERPRISE_ENG')")
    public ResponseEntity<BranchResponse> createBranch(@Valid @RequestBody BranchRequest request) {
        BranchResponse response = branchService.createBranch(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ASSOCIATION_ENG') or hasRole('ENTERPRISE_ENG')")
    public ResponseEntity<BranchResponse> updateBranch(@PathVariable Long id, @Valid @RequestBody BranchRequest request) {
        BranchResponse response = branchService.updateBranch(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ASSOCIATION_ENG')")
    public ResponseEntity<Void> deleteBranch(@PathVariable Long id) {
        branchService.deleteBranch(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BranchResponse> getBranchById(@PathVariable Long id) {
        BranchResponse response = branchService.getBranchById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<BranchResponse>> getAllBranches() {
        List<BranchResponse> responses = branchService.getAllBranches();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/enterprise/{enterpriseId}")
    public ResponseEntity<List<BranchResponse>> getBranchesByEnterprise(@PathVariable Long enterpriseId) {
        List<BranchResponse> responses = branchService.getBranchesByEnterprise(enterpriseId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/association/{associationId}")
    public ResponseEntity<List<BranchResponse>> getBranchesByAssociation(@PathVariable Long associationId) {
        List<BranchResponse> responses = branchService.getBranchesByAssociation(associationId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/search")
    public ResponseEntity<List<BranchResponse>> searchBranches(
            @RequestParam Long enterpriseId,
            @RequestParam String name) {
        List<BranchResponse> responses = branchService.searchBranches(enterpriseId, name);
        return ResponseEntity.ok(responses);
    }
}
