package com.lavanderia.productservice.dto.response;

import com.lavanderia.productservice.entity.LaundryService.PricingType;
import java.time.LocalDateTime;

public class LaundryServiceResponseDTO {
    private Long id;
    private String tenantId;
    private Long categoryId;
    private String categoryName;
    private String name;
    private PricingType pricingType;
    private Boolean isDelicate;
    private LocalDateTime createdAt;

    public LaundryServiceResponseDTO() {}

    public LaundryServiceResponseDTO(Long id, String tenantId, Long categoryId, String categoryName,
                                     String name, PricingType pricingType, Boolean isDelicate, LocalDateTime createdAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.name = name;
        this.pricingType = pricingType;
        this.isDelicate = isDelicate;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PricingType getPricingType() {
        return pricingType;
    }

    public void setPricingType(PricingType pricingType) {
        this.pricingType = pricingType;
    }

    public Boolean getIsDelicate() {
        return isDelicate;
    }

    public void setIsDelicate(Boolean isDelicate) {
        this.isDelicate = isDelicate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}