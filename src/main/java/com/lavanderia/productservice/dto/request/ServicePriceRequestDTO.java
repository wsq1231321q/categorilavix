package com.lavanderia.productservice.dto.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ServicePriceRequestDTO {
    private Long serviceId;
    private BigDecimal price;
    private LocalDateTime validFrom;
    private LocalDateTime validTo;

    public ServicePriceRequestDTO() {}

    public ServicePriceRequestDTO(Long serviceId, BigDecimal price, LocalDateTime validFrom, LocalDateTime validTo) {
        this.serviceId = serviceId;
        this.price = price;
        this.validFrom = validFrom;
        this.validTo = validTo;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDateTime getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDateTime validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDateTime getValidTo() {
        return validTo;
    }

    public void setValidTo(LocalDateTime validTo) {
        this.validTo = validTo;
    }
}