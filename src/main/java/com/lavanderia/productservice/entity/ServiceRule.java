package com.lavanderia.productservice.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "service_rules")
public class ServiceRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private LaundryService service;

    @Column(name = "requires_weight", nullable = false)
    private Boolean requiresWeight;

    @Column(name = "min_weight", precision = 10, scale = 2)
    private BigDecimal minWeight;

    @Column(name = "extra_charge", precision = 10, scale = 2)
    private BigDecimal extraCharge;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructores
    public ServiceRule() {
        this.requiresWeight = false;
        this.extraCharge = BigDecimal.ZERO;
    }

    public ServiceRule(Long id, LaundryService service, Boolean requiresWeight,
                       BigDecimal minWeight, BigDecimal extraCharge,
                       LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.service = service;
        this.requiresWeight = requiresWeight != null ? requiresWeight : false;
        this.minWeight = minWeight;
        this.extraCharge = extraCharge != null ? extraCharge : BigDecimal.ZERO;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LaundryService getService() { return service; }
    public void setService(LaundryService service) { this.service = service; }

    public Boolean getRequiresWeight() { return requiresWeight; }
    public void setRequiresWeight(Boolean requiresWeight) {
        this.requiresWeight = requiresWeight != null ? requiresWeight : false;
    }

    public BigDecimal getMinWeight() { return minWeight; }
    public void setMinWeight(BigDecimal minWeight) { this.minWeight = minWeight; }

    public BigDecimal getExtraCharge() { return extraCharge; }
    public void setExtraCharge(BigDecimal extraCharge) {
        this.extraCharge = extraCharge != null ? extraCharge : BigDecimal.ZERO;
    }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.requiresWeight == null) {
            this.requiresWeight = false;
        }
        if (this.extraCharge == null) {
            this.extraCharge = BigDecimal.ZERO;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}