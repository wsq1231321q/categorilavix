package com.lavanderia.productservice.dto.request;

import com.lavanderia.productservice.entity.LaundryService.PricingType;

public class LaundryServiceRequestDTO {
    private Long categoryId;
    private String name;
    private PricingType pricingType;
    private Boolean isDelicate;

    public LaundryServiceRequestDTO() {}

    public LaundryServiceRequestDTO(Long categoryId, String name, PricingType pricingType, Boolean isDelicate) {
        this.categoryId = categoryId;
        this.name = name;
        this.pricingType = pricingType;
        this.isDelicate = isDelicate;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
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
}