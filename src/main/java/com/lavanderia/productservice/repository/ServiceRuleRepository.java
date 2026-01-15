package com.lavanderia.productservice.repository;

import com.lavanderia.productservice.entity.ServiceRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ServiceRuleRepository extends JpaRepository<ServiceRule, Long> {
    List<ServiceRule> findByServiceId(Long serviceId);
}