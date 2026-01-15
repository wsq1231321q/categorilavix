package com.lavanderia.productservice.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ServiceRuleResponseDTO {
    private Long id;
    private Long serviceId;
    private Boolean requiresWeight;
    private BigDecimal minWeight;
    private BigDecimal extraCharge;
    private LocalDateTime createdAt;

    public ServiceRuleResponseDTO() {}

    public ServiceRuleResponseDTO(Long id, Long serviceId, Boolean requiresWeight,
                                  BigDecimal minWeight, BigDecimal extraCharge, LocalDateTime createdAt) {
        this.id = id;
        this.serviceId = serviceId;
        this.requiresWeight = requiresWeight;
        this.minWeight = minWeight;
        this.extraCharge = extraCharge;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public Boolean getRequiresWeight() {
        return requiresWeight;
    }

    public void setRequiresWeight(Boolean requiresWeight) {
        this.requiresWeight = requiresWeight;
    }

    public BigDecimal getMinWeight() {
        return minWeight;
    }

    public void setMinWeight(BigDecimal minWeight) {
        this.minWeight = minWeight;
    }

    public BigDecimal getExtraCharge() {
        return extraCharge;
    }

    public void setExtraCharge(BigDecimal extraCharge) {
        this.extraCharge = extraCharge;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}