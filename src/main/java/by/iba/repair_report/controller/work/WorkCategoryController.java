package by.iba.repair_report.controller.work;

import by.iba.repair_report.dto.request.WorkCategoryRequest;
import by.iba.repair_report.dto.response.WorkCategoryResponse;
import by.iba.repair_report.entity.WorkCategory;
import by.iba.repair_report.service.WorkCategoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/work-categories")
public class WorkCategoryController {

    private final WorkCategoryService workCategoryService;

    public WorkCategoryController(WorkCategoryService workCategoryService) {
        this.workCategoryService = workCategoryService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ASSOCIATION_ENG')")
    public ResponseEntity<WorkCategoryResponse> createWorkCategory(@Valid @RequestBody WorkCategoryRequest request) {
        WorkCategoryResponse response = workCategoryService.createWorkCategory(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ASSOCIATION_ENG')")
    public ResponseEntity<WorkCategoryResponse> updateWorkCategory(@PathVariable Long id, @Valid @RequestBody WorkCategoryRequest request) {
        WorkCategoryResponse response = workCategoryService.updateWorkCategory(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ASSOCIATION_ENG')")
    public ResponseEntity<Void> deleteWorkCategory(@PathVariable Long id) {
        workCategoryService.deleteWorkCategory(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkCategoryResponse> getWorkCategoryById(@PathVariable Long id) {
        WorkCategoryResponse response = workCategoryService.getWorkCategoryById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<WorkCategoryResponse>> getAllWorkCategories() {
        List<WorkCategoryResponse> responses = workCategoryService.getAllWorkCategories();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/active")
    public ResponseEntity<List<WorkCategoryResponse>> getActiveWorkCategories() {
        List<WorkCategoryResponse> responses = workCategoryService.getActiveWorkCategories();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/network-type/{networkType}")
    public ResponseEntity<List<WorkCategoryResponse>> getWorkCategoriesByNetworkType(@PathVariable WorkCategory.NetworkType networkType) {
        List<WorkCategoryResponse> responses = workCategoryService.getWorkCategoriesByNetworkType(networkType);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/search")
    public ResponseEntity<List<WorkCategoryResponse>> searchWorkCategories(@RequestParam String name) {
        List<WorkCategoryResponse> responses = workCategoryService.searchWorkCategories(name);
        return ResponseEntity.ok(responses);
    }
}
