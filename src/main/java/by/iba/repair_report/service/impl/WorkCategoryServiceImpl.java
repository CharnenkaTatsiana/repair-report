package by.iba.repair_report.service.impl;

import by.iba.repair_report.dto.request.WorkCategoryRequest;
import by.iba.repair_report.dto.response.WorkCategoryResponse;
import by.iba.repair_report.entity.WorkCategory;
import by.iba.repair_report.exception.ResourceNotFoundException;
import by.iba.repair_report.repository.WorkCategoryRepository;
import by.iba.repair_report.service.WorkCategoryService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkCategoryServiceImpl implements WorkCategoryService {

    private final WorkCategoryRepository workCategoryRepository;

    public WorkCategoryServiceImpl(WorkCategoryRepository workCategoryRepository) {
        this.workCategoryRepository = workCategoryRepository;
    }

    @Override
    @Transactional
    public WorkCategoryResponse createWorkCategory(WorkCategoryRequest request) {
        if (workCategoryRepository.existsByName(request.getName())) {
            throw new RuntimeException("Work category with this name already exists");
        }

        WorkCategory workCategory = new WorkCategory(request.getName(), request.getNetworkType());
        workCategory.setIsActive(request.getIsActive());

        WorkCategory savedWorkCategory = workCategoryRepository.save(workCategory);
        return convertToResponse(savedWorkCategory);
    }

    @Override
    @Transactional
    public WorkCategoryResponse updateWorkCategory(Long id, WorkCategoryRequest request) {
        WorkCategory workCategory = workCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Work category not found"));

        if (!workCategory.getName().equals(request.getName()) &&
                workCategoryRepository.existsByNameAndIdNot(request.getName(), id)) {
            throw new RuntimeException("Work category with this name already exists");
        }

        workCategory.setName(request.getName());
        workCategory.setNetworkType(request.getNetworkType());
        workCategory.setIsActive(request.getIsActive());

        WorkCategory updatedWorkCategory = workCategoryRepository.save(workCategory);
        return convertToResponse(updatedWorkCategory);
    }

    @Override
    @Transactional
    public void deleteWorkCategory(Long id) {
        WorkCategory workCategory = workCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Work category not found"));

        // Вместо удаления деактивируем категорию
        workCategory.setIsActive(false);
        workCategoryRepository.save(workCategory);
    }

    @Override
    public WorkCategoryResponse getWorkCategoryById(Long id) {
        WorkCategory workCategory = workCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Work category not found"));
        return convertToResponse(workCategory);
    }

    @Override
    public List<WorkCategoryResponse> getAllWorkCategories() {
        return workCategoryRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkCategoryResponse> getActiveWorkCategories() {
        return workCategoryRepository.findByIsActiveTrue().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkCategoryResponse> getWorkCategoriesByNetworkType(WorkCategory.NetworkType networkType) {
        return workCategoryRepository.findByNetworkTypeAndIsActive(networkType, true).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkCategoryResponse> searchWorkCategories(String name) {
        return workCategoryRepository.findByNameContainingIgnoreCaseAndActive(name).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public WorkCategory getWorkCategoryEntity(Long id) {
        return workCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Work category not found"));
    }

    private WorkCategoryResponse convertToResponse(WorkCategory workCategory) {
        WorkCategoryResponse response = new WorkCategoryResponse();
        response.setId(workCategory.getId());
        response.setName(workCategory.getName());
        response.setNetworkType(workCategory.getNetworkType().name());
        response.setIsActive(workCategory.getIsActive());
        return response;
    }
}
