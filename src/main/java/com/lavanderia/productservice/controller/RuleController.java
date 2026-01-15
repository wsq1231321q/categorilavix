package com.lavanderia.productservice.controller;

import com.lavanderia.productservice.dto.request.ServiceRuleDTO;
import com.lavanderia.productservice.dto.response.ServiceRuleResponseDTO;
import com.lavanderia.productservice.service.RuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reglas")
@Tag(name = "Gestión de Reglas", description = "APIs para administrar reglas de servicios")
public class RuleController {

    private final RuleService ruleService;

    @Autowired
    public RuleController(RuleService ruleService) {
        this.ruleService = ruleService;
    }

    @PostMapping
    @Operation(summary = "Crear nueva regla",
            description = "Crea una nueva regla para un servicio específico")
    public ResponseEntity<ServiceRuleResponseDTO> crearRegla(
            @Parameter(description = "ID del tenant (obligatorio en header)", required = true)
            @RequestHeader("X-Tenant-ID") String tenantId,
            @RequestBody ServiceRuleDTO dto) {
        return new ResponseEntity<>(ruleService.createRule(tenantId, dto), HttpStatus.CREATED);
    }

    @GetMapping("/por-servicio/{servicioId}")
    @Operation(summary = "Obtener reglas por servicio",
            description = "Devuelve todas las reglas asociadas a un servicio específico")
    public ResponseEntity<List<ServiceRuleResponseDTO>> obtenerReglasPorServicio(
            @Parameter(description = "ID del tenant (obligatorio en header)", required = true)
            @RequestHeader("X-Tenant-ID") String tenantId,
            @Parameter(description = "ID del servicio", required = true)
            @PathVariable Long servicioId) {
        return ResponseEntity.ok(ruleService.getRulesByService(servicioId, tenantId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener regla por ID",
            description = "Devuelve una regla específica verificando que pertenezca al tenant")
    public ResponseEntity<ServiceRuleResponseDTO> obtenerReglaPorId(
            @Parameter(description = "ID del tenant (obligatorio en header)", required = true)
            @RequestHeader("X-Tenant-ID") String tenantId,
            @Parameter(description = "ID de la regla", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(ruleService.getRuleById(id, tenantId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar regla",
            description = "Actualiza los datos de una regla existente")
    public ResponseEntity<ServiceRuleResponseDTO> actualizarRegla(
            @Parameter(description = "ID del tenant (obligatorio en header)", required = true)
            @RequestHeader("X-Tenant-ID") String tenantId,
            @Parameter(description = "ID de la regla", required = true)
            @PathVariable Long id,
            @RequestBody ServiceRuleDTO dto) {
        return ResponseEntity.ok(ruleService.updateRule(id, tenantId, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar regla",
            description = "Elimina permanentemente una regla del sistema")
    public ResponseEntity<Void> eliminarRegla(
            @Parameter(description = "ID del tenant (obligatorio en header)", required = true)
            @RequestHeader("X-Tenant-ID") String tenantId,
            @Parameter(description = "ID de la regla", required = true)
            @PathVariable Long id) {
        ruleService.deleteRule(id, tenantId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/requiere-peso/{servicioId}")
    @Operation(summary = "Verificar si servicio requiere peso",
            description = "Devuelve si un servicio tiene reglas que requieren especificar peso")
    public ResponseEntity<Boolean> servicioRequierePeso(
            @Parameter(description = "ID del tenant (obligatorio en header)", required = true)
            @RequestHeader("X-Tenant-ID") String tenantId,
            @Parameter(description = "ID del servicio", required = true)
            @PathVariable Long servicioId) {
        return ResponseEntity.ok(ruleService.serviceRequiresWeight(servicioId, tenantId));
    }
}