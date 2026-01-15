package com.lavanderia.productservice.dto.request;

import java.math.BigDecimal;

public class ServiceRuleDTO {
    private Long serviceId;
    private Boolean requiresWeight;
    private BigDecimal minWeight;
    private BigDecimal extraCharge;

    public ServiceRuleDTO() {}

    public ServiceRuleDTO(Long serviceId, Boolean requiresWeight, BigDecimal minWeight, BigDecimal extraCharge) {
        this.serviceId = serviceId;
        this.requiresWeight = requiresWeight;
        this.minWeight = minWeight;
        this.extraCharge = extraCharge;
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
}