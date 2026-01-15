package com.lavanderia.productservice.dto.response;

import java.time.LocalDateTime;

public class ServiceCategoryResponseDTO {
    private Long id;
    private String tenantId;
    private String name;
    private LocalDateTime createdAt;

    public ServiceCategoryResponseDTO() {}

    public ServiceCategoryResponseDTO(Long id, String tenantId, String name, LocalDateTime createdAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}