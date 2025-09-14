package by.iba.repair_report.controller.structure;

import by.iba.repair_report.dto.request.EnterpriseRequest;
import by.iba.repair_report.dto.response.EnterpriseResponse;
import by.iba.repair_report.service.EnterpriseService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enterprises")
public class EnterpriseController {

    private final EnterpriseService enterpriseService;

    public EnterpriseController(EnterpriseService enterpriseService) {
        this.enterpriseService = enterpriseService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('ASSOCIATION_ENG')")
    public ResponseEntity<EnterpriseResponse> createEnterprise(@Valid @RequestBody EnterpriseRequest request) {
        EnterpriseResponse response = enterpriseService.createEnterprise(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ASSOCIATION_ENG')")
    public ResponseEntity<EnterpriseResponse> updateEnterprise(@PathVariable Long id, @Valid @RequestBody EnterpriseRequest request) {
        EnterpriseResponse response = enterpriseService.updateEnterprise(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ASSOCIATION_ENG')")
    public ResponseEntity<Void> deleteEnterprise(@PathVariable Long id) {
        enterpriseService.deleteEnterprise(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnterpriseResponse> getEnterpriseById(@PathVariable Long id) {
        EnterpriseResponse response = enterpriseService.getEnterpriseById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<EnterpriseResponse>> getAllEnterprises() {
        List<EnterpriseResponse> responses = enterpriseService.getAllEnterprises();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/association/{associationId}")
    public ResponseEntity<List<EnterpriseResponse>> getEnterprisesByAssociation(@PathVariable Long associationId) {
        List<EnterpriseResponse> responses = enterpriseService.getEnterprisesByAssociation(associationId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/search")
    public ResponseEntity<List<EnterpriseResponse>> searchEnterprises(
            @RequestParam Long associationId,
            @RequestParam String name) {
        List<EnterpriseResponse> responses = enterpriseService.searchEnterprises(associationId, name);
        return ResponseEntity.ok(responses);
    }
}
