package com.lavanderia.productservice.service;

import com.lavanderia.productservice.dto.request.ServiceRuleDTO;
import com.lavanderia.productservice.dto.response.ServiceRuleResponseDTO;
import com.lavanderia.productservice.entity.LaundryService;
import com.lavanderia.productservice.entity.ServiceRule;
import com.lavanderia.productservice.exception.ResourceNotFoundException;
import com.lavanderia.productservice.exception.TenantAccessException;
import com.lavanderia.productservice.repository.LaundryServiceRepository;
import com.lavanderia.productservice.repository.ServiceRuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RuleService {

    private final ServiceRuleRepository ruleRepository;
    private final LaundryServiceRepository laundryServiceRepository;

    @Autowired
    public RuleService(ServiceRuleRepository ruleRepository, LaundryServiceRepository laundryServiceRepository) {
        this.ruleRepository = ruleRepository;
        this.laundryServiceRepository = laundryServiceRepository;
    }

    @Transactional
    public ServiceRuleResponseDTO createRule(String tenantId, ServiceRuleDTO dto) {
        LaundryService service = laundryServiceRepository.findById(dto.getServiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Service not found"));

        if (!service.getTenantId().equals(tenantId)) {
            throw new TenantAccessException("Unauthorized access");
        }

        ServiceRule rule = new ServiceRule();
        rule.setService(service);
        rule.setRequiresWeight(dto.getRequiresWeight());
        rule.setMinWeight(dto.getMinWeight());
        rule.setExtraCharge(dto.getExtraCharge());
        rule.setCreatedAt(LocalDateTime.now());
        rule.setUpdatedAt(LocalDateTime.now());

        rule = ruleRepository.save(rule);
        return mapToResponse(rule);
    }

    public List<ServiceRuleResponseDTO> getRulesByService(Long serviceId, String tenantId) {
        LaundryService service = laundryServiceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found"));

        if (!service.getTenantId().equals(tenantId)) {
            throw new TenantAccessException("Unauthorized access");
        }

        return ruleRepository.findByServiceId(serviceId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ServiceRuleResponseDTO getRuleById(Long id, String tenantId) {
        ServiceRule rule = ruleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rule not found with ID: " + id));

        if (!rule.getService().getTenantId().equals(tenantId)) {
            throw new TenantAccessException("Unauthorized access to rule");
        }

        return mapToResponse(rule);
    }

    @Transactional
    public ServiceRuleResponseDTO updateRule(Long id, String tenantId, ServiceRuleDTO dto) {
        ServiceRule rule = ruleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rule not found with ID: " + id));

        if (!rule.getService().getTenantId().equals(tenantId)) {
            throw new TenantAccessException("Unauthorized access to rule");
        }

        rule.setRequiresWeight(dto.getRequiresWeight());
        rule.setMinWeight(dto.getMinWeight());
        rule.setExtraCharge(dto.getExtraCharge());
        rule.setUpdatedAt(LocalDateTime.now());

        rule = ruleRepository.save(rule);
        return mapToResponse(rule);
    }

    @Transactional
    public void deleteRule(Long id, String tenantId) {
        ServiceRule rule = ruleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rule not found with ID: " + id));

        if (!rule.getService().getTenantId().equals(tenantId)) {
            throw new TenantAccessException("Unauthorized access to rule");
        }

        ruleRepository.delete(rule);
    }

    public boolean serviceRequiresWeight(Long serviceId, String tenantId) {
        List<ServiceRule> rules = ruleRepository.findByServiceId(serviceId);
        if (rules.isEmpty()) {
            return false;
        }

        // Verificar que el servicio pertenezca al tenant
        LaundryService service = laundryServiceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with ID: " + serviceId));

        if (!service.getTenantId().equals(tenantId)) {
            throw new TenantAccessException("Unauthorized access to service");
        }

        return rules.stream().anyMatch(rule -> Boolean.TRUE.equals(rule.getRequiresWeight()));
    }

    private ServiceRuleResponseDTO mapToResponse(ServiceRule rule) {
        return new ServiceRuleResponseDTO(
                rule.getId(),
                rule.getService().getId(),
                rule.getRequiresWeight(),
                rule.getMinWeight(),
                rule.getExtraCharge(),
                rule.getCreatedAt()
        );
    }
}