package by.iba.repair_report.controller.structure;

import by.iba.repair_report.dto.request.AssociationRequest;
import by.iba.repair_report.dto.response.AssociationResponse;
import by.iba.repair_report.service.AssociationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/associations")
public class AssociationController {

    private final AssociationService associationService;

    public AssociationController(AssociationService associationService) {
        this.associationService = associationService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('ASSOCIATION_ENG')")
    public ResponseEntity<AssociationResponse> createAssociation(@Valid @RequestBody AssociationRequest request) {
        AssociationResponse response = associationService.createAssociation(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ASSOCIATION_ENG')")
    public ResponseEntity<AssociationResponse> updateAssociation(@PathVariable Long id, @Valid @RequestBody AssociationRequest request) {
        AssociationResponse response = associationService.updateAssociation(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAssociation(@PathVariable Long id) {
        associationService.deleteAssociation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssociationResponse> getAssociationById(@PathVariable Long id) {
        AssociationResponse response = associationService.getAssociationById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<AssociationResponse>> getAllAssociations() {
        List<AssociationResponse> responses = associationService.getAllAssociations();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/search")
    public ResponseEntity<List<AssociationResponse>> searchAssociations(@RequestParam String name) {
        List<AssociationResponse> responses = associationService.searchAssociations(name);
        return ResponseEntity.ok(responses);
    }
}
