package com.lavanderia.productservice.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ServicePriceResponseDTO {
    private Long id;
    private Long serviceId;
    private BigDecimal price;
    private LocalDateTime validFrom;
    private LocalDateTime validTo;
    private LocalDateTime createdAt;

    public ServicePriceResponseDTO() {}

    public ServicePriceResponseDTO(Long id, Long serviceId, BigDecimal price,
                                   LocalDateTime validFrom, LocalDateTime validTo, LocalDateTime createdAt) {
        this.id = id;
        this.serviceId = serviceId;
        this.price = price;
        this.validFrom = validFrom;
        this.validTo = validTo;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}