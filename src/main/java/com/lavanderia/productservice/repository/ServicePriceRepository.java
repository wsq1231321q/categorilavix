package com.lavanderia.productservice.repository;

import com.lavanderia.productservice.entity.ServicePrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ServicePriceRepository extends JpaRepository<ServicePrice, Long> {

    List<ServicePrice> findByServiceId(Long serviceId);

    @Query("SELECT p FROM ServicePrice p WHERE p.service.id = :serviceId " +
            "AND p.validFrom <= :currentDate " +
            "AND (p.validTo IS NULL OR p.validTo >= :currentDate)")
    Optional<ServicePrice> findActivePrice(@Param("serviceId") Long serviceId,
                                           @Param("currentDate") LocalDateTime currentDate);

    List<ServicePrice> findByServiceIdAndValidFromBetween(Long serviceId, LocalDateTime start, LocalDateTime end);

    // Método para buscar por servicio y rango de fechas
    @Query("SELECT p FROM ServicePrice p WHERE p.service.id = :serviceId " +
            "AND p.validFrom <= :endDate " +
            "AND (p.validTo IS NULL OR p.validTo >= :startDate)")
    List<ServicePrice> findByServiceIdAndDateRange(@Param("serviceId") Long serviceId,
                                                   @Param("startDate") LocalDateTime startDate,
                                                   @Param("endDate") LocalDateTime endDate);

    // Método alternativo usando la convención de nombres de Spring Data JPA
    Optional<ServicePrice> findFirstByServiceIdAndValidFromLessThanEqualAndValidToGreaterThanEqualOrValidToIsNullOrderByValidFromDesc(
            Long serviceId, LocalDateTime date1, LocalDateTime date2);
}