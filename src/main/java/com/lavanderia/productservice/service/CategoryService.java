package com.lavanderia.productservice.service;

import com.lavanderia.productservice.dto.request.ServiceCategoryRequestDTO;
import com.lavanderia.productservice.dto.response.ServiceCategoryResponseDTO;
import com.lavanderia.productservice.entity.ServiceCategory;
import com.lavanderia.productservice.exception.ResourceNotFoundException;
import com.lavanderia.productservice.exception.TenantAccessException;
import com.lavanderia.productservice.repository.ServiceCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final ServiceCategoryRepository categoryRepository;

    @Autowired
    public CategoryService(ServiceCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public ServiceCategoryResponseDTO createCategory(String tenantId, ServiceCategoryRequestDTO dto) {
        ServiceCategory category = new ServiceCategory();
        category.setTenantId(tenantId);
        category.setName(dto.getName());
        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());

        category = categoryRepository.save(category);
        return mapToResponse(category);
    }

    @Transactional
    public ServiceCategoryResponseDTO updateCategory(Long id, String tenantId, ServiceCategoryRequestDTO dto) {
        ServiceCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        if (!category.getTenantId().equals(tenantId)) {
            throw new TenantAccessException("Unauthorized access to category");
        }

        category.setName(dto.getName());
        category.setUpdatedAt(LocalDateTime.now());
        category = categoryRepository.save(category);
        return mapToResponse(category);
    }

    @Transactional
    public void deleteCategory(Long id, String tenantId) {
        ServiceCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        if (!category.getTenantId().equals(tenantId)) {
            throw new TenantAccessException("Unauthorized access to category");
        }

        // Verificar si tiene servicios asociados
        if (category.getServices() != null && !category.getServices().isEmpty()) {
            throw new IllegalStateException("Cannot delete category with associated services");
        }

        categoryRepository.delete(category);
    }

    public List<ServiceCategoryResponseDTO> getAllCategoriesByTenant(String tenantId) {
        return categoryRepository.findByTenantId(tenantId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ServiceCategoryResponseDTO getCategoryById(Long id, String tenantId) {
        ServiceCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        if (!category.getTenantId().equals(tenantId)) {
            throw new TenantAccessException("Unauthorized access");
        }

        return mapToResponse(category);
    }

    public List<ServiceCategoryResponseDTO> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private ServiceCategoryResponseDTO mapToResponse(ServiceCategory category) {
        return new ServiceCategoryResponseDTO(
                category.getId(),
                category.getTenantId(),
                category.getName(),
                category.getCreatedAt()
        );
    }
}