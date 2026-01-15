package com.lavanderia.productservice.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "services")
public class LaundryService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private ServiceCategory category;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "pricing_type", nullable = false)
    private PricingType pricingType;

    @Column(name = "is_delicate")
    private Boolean isDelicate;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ServicePrice> prices;

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ServiceRule> rules;

    // Enums
    public enum PricingType {
        PER_ITEM, PER_KG
    }

    // Constructores
    public LaundryService() {
        this.isDelicate = false;
    }

    public LaundryService(Long id, String tenantId, ServiceCategory category, String name,
                          PricingType pricingType, Boolean isDelicate, LocalDateTime createdAt,
                          LocalDateTime updatedAt, List<ServicePrice> prices, List<ServiceRule> rules) {
        this.id = id;
        this.tenantId = tenantId;
        this.category = category;
        this.name = name;
        this.pricingType = pricingType;
        this.isDelicate = isDelicate != null ? isDelicate : false;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.prices = prices;
        this.rules = rules;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }

    public ServiceCategory getCategory() { return category; }
    public void setCategory(ServiceCategory category) { this.category = category; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public PricingType getPricingType() { return pricingType; }
    public void setPricingType(PricingType pricingType) { this.pricingType = pricingType; }

    public Boolean getIsDelicate() { return isDelicate; }
    public void setIsDelicate(Boolean isDelicate) { this.isDelicate = isDelicate; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public List<ServicePrice> getPrices() { return prices; }
    public void setPrices(List<ServicePrice> prices) { this.prices = prices; }

    public List<ServiceRule> getRules() { return rules; }
    public void setRules(List<ServiceRule> rules) { this.rules = rules; }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.isDelicate == null) {
            this.isDelicate = false;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}