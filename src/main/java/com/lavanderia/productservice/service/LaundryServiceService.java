package com.lavanderia.productservice.service;

import com.lavanderia.productservice.dto.request.LaundryServiceRequestDTO;
import com.lavanderia.productservice.dto.response.LaundryServiceResponseDTO;
import com.lavanderia.productservice.entity.LaundryService;
import com.lavanderia.productservice.entity.ServiceCategory;
import com.lavanderia.productservice.exception.ResourceNotFoundException;
import com.lavanderia.productservice.exception.TenantAccessException;
import com.lavanderia.productservice.repository.LaundryServiceRepository;
import com.lavanderia.productservice.repository.ServiceCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LaundryServiceService {

    private final LaundryServiceRepository laundryServiceRepository;
    private final ServiceCategoryRepository categoryRepository;

    @Autowired
    public LaundryServiceService(LaundryServiceRepository laundryServiceRepository,
                                 ServiceCategoryRepository categoryRepository) {
        this.laundryServiceRepository = laundryServiceRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public LaundryServiceResponseDTO createService(String tenantId, LaundryServiceRequestDTO dto) {
        ServiceCategory category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        if (!category.getTenantId().equals(tenantId)) {
            throw new TenantAccessException("Unauthorized access");
        }

        LaundryService service = new LaundryService();
        service.setTenantId(tenantId);
        service.setCategory(category);
        service.setName(dto.getName());
        service.setPricingType(dto.getPricingType());
        service.setIsDelicate(dto.getIsDelicate());
        service.setCreatedAt(LocalDateTime.now());
        service.setUpdatedAt(LocalDateTime.now());

        service = laundryServiceRepository.save(service);
        return mapToResponse(service);
    }

    public List<LaundryServiceResponseDTO> getAllServicesByTenant(String tenantId) {
        return laundryServiceRepository.findByTenantId(tenantId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public LaundryServiceResponseDTO getServiceById(Long id, String tenantId) {
        LaundryService service = laundryServiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found"));

        if (!service.getTenantId().equals(tenantId)) {
            throw new TenantAccessException("Unauthorized access");
        }

        return mapToResponse(service);
    }

    public List<LaundryServiceResponseDTO> getServicesByCategory(Long categoryId, String tenantId) {
        ServiceCategory category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        if (!category.getTenantId().equals(tenantId)) {
            throw new TenantAccessException("Unauthorized access");
        }

        return laundryServiceRepository.findByCategoryId(categoryId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<LaundryServiceResponseDTO> getAllServices() {
        return laundryServiceRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public LaundryServiceResponseDTO updateService(Long id, String tenantId, LaundryServiceRequestDTO dto) {
        LaundryService service = laundryServiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with id: " + id));

        if (!service.getTenantId().equals(tenantId)) {
            throw new TenantAccessException("Unauthorized access to service");
        }

        ServiceCategory category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + dto.getCategoryId()));

        service.setCategory(category);
        service.setName(dto.getName());
        service.setPricingType(dto.getPricingType());
        service.setIsDelicate(dto.getIsDelicate());
        service.setUpdatedAt(LocalDateTime.now());

        service = laundryServiceRepository.save(service);
        return mapToResponse(service);
    }

    @Transactional
    public void deleteService(Long id, String tenantId) {
        LaundryService service = laundryServiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with id: " + id));

        if (!service.getTenantId().equals(tenantId)) {
            throw new TenantAccessException("Unauthorized access to service");
        }

        // Verificar si tiene precios o reglas asociadas
        if (service.getPrices() != null && !service.getPrices().isEmpty()) {
            throw new IllegalStateException("Cannot delete service with associated prices");
        }

        if (service.getRules() != null && !service.getRules().isEmpty()) {
            throw new IllegalStateException("Cannot delete service with associated rules");
        }

        laundryServiceRepository.delete(service);
    }

    public List<LaundryServiceResponseDTO> getDelicateServicesByTenant(String tenantId) {
        // Si el repositorio no tiene el método findByTenantIdAndIsDelicate, usamos un filtro en Java
        return laundryServiceRepository.findByTenantId(tenantId)
                .stream()
                .filter(service -> Boolean.TRUE.equals(service.getIsDelicate()))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private LaundryServiceResponseDTO mapToResponse(LaundryService service) {
        return new LaundryServiceResponseDTO(
                service.getId(),
                service.getTenantId(),
                service.getCategory().getId(),
                service.getCategory().getName(),
                service.getName(),
                service.getPricingType(),
                service.getIsDelicate(),
                service.getCreatedAt()
        );
    }
}