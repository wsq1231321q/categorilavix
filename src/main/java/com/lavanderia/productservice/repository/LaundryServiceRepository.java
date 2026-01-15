package com.lavanderia.productservice.repository;

import com.lavanderia.productservice.entity.LaundryService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LaundryServiceRepository extends JpaRepository<LaundryService, Long> {
    List<LaundryService> findByTenantId(String tenantId);
    List<LaundryService> findByCategoryId(Long categoryId);

    @Query("SELECT s FROM LaundryService s WHERE s.tenantId = :tenantId AND s.category.id = :categoryId")
    List<LaundryService> findByTenantIdAndCategoryId(String tenantId, Long categoryId);

    List<LaundryService> findByTenantIdAndIsDelicate(String tenantId, Boolean isDelicate);
}