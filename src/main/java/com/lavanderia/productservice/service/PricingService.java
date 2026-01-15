package com.lavanderia.productservice.service;

import com.lavanderia.productservice.dto.request.ServicePriceRequestDTO;
import com.lavanderia.productservice.dto.response.ServicePriceResponseDTO;
import com.lavanderia.productservice.entity.LaundryService;
import com.lavanderia.productservice.entity.ServicePrice;
import com.lavanderia.productservice.exception.ResourceNotFoundException;
import com.lavanderia.productservice.exception.TenantAccessException;
import com.lavanderia.productservice.repository.LaundryServiceRepository;
import com.lavanderia.productservice.repository.ServicePriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PricingService {

    private final ServicePriceRepository priceRepository;
    private final LaundryServiceRepository laundryServiceRepository;

    @Autowired
    public PricingService(ServicePriceRepository priceRepository, LaundryServiceRepository laundryServiceRepository) {
        this.priceRepository = priceRepository;
        this.laundryServiceRepository = laundryServiceRepository;
    }

    @Transactional
    public ServicePriceResponseDTO createPrice(String tenantId, ServicePriceRequestDTO dto) {
        LaundryService service = laundryServiceRepository.findById(dto.getServiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with id: " + dto.getServiceId()));

        if (!service.getTenantId().equals(tenantId)) {
            throw new TenantAccessException("Unauthorized access to service");
        }

        ServicePrice price = new ServicePrice();
        price.setService(service);
        price.setPrice(dto.getPrice());
        price.setValidFrom(dto.getValidFrom() != null ? dto.getValidFrom() : LocalDateTime.now());
        price.setValidTo(dto.getValidTo());
        price.setCreatedAt(LocalDateTime.now());
        price.setUpdatedAt(LocalDateTime.now());

        price = priceRepository.save(price);
        return mapToResponse(price);
    }

    public List<ServicePriceResponseDTO> getPricesByService(Long serviceId, String tenantId) {
        LaundryService service = laundryServiceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with id: " + serviceId));

        if (!service.getTenantId().equals(tenantId)) {
            throw new TenantAccessException("Unauthorized access to service");
        }

        return priceRepository.findByServiceId(serviceId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ServicePriceResponseDTO getActivePrice(Long serviceId, String tenantId) {
        LaundryService service = laundryServiceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with id: " + serviceId));

        if (!service.getTenantId().equals(tenantId)) {
            throw new TenantAccessException("Unauthorized access to service");
        }

        LocalDateTime now = LocalDateTime.now();
        // Usamos el método findActivePrice que definimos con @Query
        return priceRepository.findActivePrice(serviceId, now)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("No active price found for service id: " + serviceId));
    }

    public ServicePriceResponseDTO getActivePriceByServiceId(Long serviceId) {
        LocalDateTime now = LocalDateTime.now();
        return priceRepository.findActivePrice(serviceId, now)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("No active price found for service id: " + serviceId));
    }

    public List<ServicePriceResponseDTO> getHistoricalPrices(Long serviceId, String tenantId,
                                                             LocalDateTime startDate, LocalDateTime endDate) {
        LaundryService service = laundryServiceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with id: " + serviceId));

        if (!service.getTenantId().equals(tenantId)) {
            throw new TenantAccessException("Unauthorized access to service");
        }

        // Validar fechas
        if (startDate == null) {
            startDate = LocalDateTime.now().minusYears(1);
        }
        if (endDate == null) {
            endDate = LocalDateTime.now();
        }

        return priceRepository.findByServiceIdAndValidFromBetween(serviceId, startDate, endDate)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ServicePriceResponseDTO updatePrice(Long priceId, String tenantId, ServicePriceRequestDTO dto) {
        ServicePrice price = priceRepository.findById(priceId)
                .orElseThrow(() -> new ResourceNotFoundException("Price not found with id: " + priceId));

        if (!price.getService().getTenantId().equals(tenantId)) {
            throw new TenantAccessException("Unauthorized access to price");
        }

        price.setPrice(dto.getPrice());
        price.setValidFrom(dto.getValidFrom());
        price.setValidTo(dto.getValidTo());
        price.setUpdatedAt(LocalDateTime.now());

        price = priceRepository.save(price);
        return mapToResponse(price);
    }

    @Transactional
    public void deletePrice(Long priceId, String tenantId) {
        ServicePrice price = priceRepository.findById(priceId)
                .orElseThrow(() -> new ResourceNotFoundException("Price not found with id: " + priceId));

        if (!price.getService().getTenantId().equals(tenantId)) {
            throw new TenantAccessException("Unauthorized access to price");
        }

        priceRepository.delete(price);
    }

    @Transactional
    public ServicePriceResponseDTO deactivatePrice(Long priceId, String tenantId) {
        ServicePrice price = priceRepository.findById(priceId)
                .orElseThrow(() -> new ResourceNotFoundException("Price not found with id: " + priceId));

        if (!price.getService().getTenantId().equals(tenantId)) {
            throw new TenantAccessException("Unauthorized access to price");
        }

        price.setValidTo(LocalDateTime.now());
        price.setUpdatedAt(LocalDateTime.now());
        price = priceRepository.save(price);
        return mapToResponse(price);
    }

    public boolean hasOverlappingPrice(Long serviceId, LocalDateTime newValidFrom, LocalDateTime newValidTo) {
        // Si no existe el método findByServiceIdAndDateRange, lo implementamos manualmente
        List<ServicePrice> existingPrices = priceRepository.findByServiceId(serviceId)
                .stream()
                .filter(price -> {
                    LocalDateTime priceValidFrom = price.getValidFrom();
                    LocalDateTime priceValidTo = price.getValidTo();

                    // Si newValidTo es null, significa que no tiene fecha de fin (siempre activo)
                    if (newValidTo == null) {
                        return !priceValidFrom.isAfter(newValidFrom) &&
                                (priceValidTo == null || priceValidTo.isAfter(newValidFrom));
                    }

                    // Si priceValidTo es null, significa que el precio existente no tiene fecha de fin
                    if (priceValidTo == null) {
                        return !priceValidFrom.isAfter(newValidTo);
                    }

                    // Ambos tienen fechas de fin, verificar superposición
                    return !priceValidFrom.isAfter(newValidTo) && !newValidFrom.isAfter(priceValidTo);
                })
                .collect(Collectors.toList());

        return !existingPrices.isEmpty();
    }

    private ServicePriceResponseDTO mapToResponse(ServicePrice price) {
        return new ServicePriceResponseDTO(
                price.getId(),
                price.getService().getId(),
                price.getPrice(),
                price.getValidFrom(),
                price.getValidTo(),
                price.getCreatedAt()
        );
    }
}