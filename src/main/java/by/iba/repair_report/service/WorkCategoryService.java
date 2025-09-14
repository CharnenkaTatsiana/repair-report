package by.iba.repair_report.service;

import by.iba.repair_report.dto.request.WorkCategoryRequest;
import by.iba.repair_report.dto.response.WorkCategoryResponse;
import by.iba.repair_report.entity.WorkCategory;

import java.util.List;

public interface WorkCategoryService {
    WorkCategoryResponse createWorkCategory(WorkCategoryRequest request);
    WorkCategoryResponse updateWorkCategory(Long id, WorkCategoryRequest request);
    void deleteWorkCategory(Long id);
    WorkCategoryResponse getWorkCategoryById(Long id);
    List<WorkCategoryResponse> getAllWorkCategories();
    List<WorkCategoryResponse> getActiveWorkCategories();
    List<WorkCategoryResponse> getWorkCategoriesByNetworkType(WorkCategory.NetworkType networkType);
    List<WorkCategoryResponse> searchWorkCategories(String name);
    WorkCategory getWorkCategoryEntity(Long id);
}
