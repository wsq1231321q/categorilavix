package com.lavanderia.productservice.dto.request;

public class ServiceCategoryRequestDTO {
    private String name;

    public ServiceCategoryRequestDTO() {}

    public ServiceCategoryRequestDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}