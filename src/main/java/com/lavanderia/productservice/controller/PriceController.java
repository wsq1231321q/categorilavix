package com.lavanderia.productservice.controller;

import com.lavanderia.productservice.dto.request.ServicePriceRequestDTO;
import com.lavanderia.productservice.dto.response.ServicePriceResponseDTO;
import com.lavanderia.productservice.service.PricingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/precios")
@Tag(name = "Gestión de Precios", description = "APIs para administrar precios de servicios")
public class PriceController {

    private final PricingService pricingService;

    @Autowired
    public PriceController(PricingService pricingService) {
        this.pricingService = pricingService;
    }

    @PostMapping
    @Operation(summary = "Crear nuevo precio",
            description = "Crea un nuevo precio para un servicio, validando que no se superponga con precios existentes")
    public ResponseEntity<ServicePriceResponseDTO> crearPrecio(
            @Parameter(description = "ID del tenant (obligatorio en header)", required = true)
            @RequestHeader("X-Tenant-ID") String tenantId,
            @RequestBody ServicePriceRequestDTO dto) {
        return new ResponseEntity<>(pricingService.createPrice(tenantId, dto), HttpStatus.CREATED);
    }

    @GetMapping("/por-servicio/{servicioId}")
    @Operation(summary = "Obtener precios por servicio",
            description = "Devuelve todos los precios históricos de un servicio específico")
    public ResponseEntity<List<ServicePriceResponseDTO>> obtenerPreciosPorServicio(
            @Parameter(description = "ID del tenant (obligatorio en header)", required = true)
            @RequestHeader("X-Tenant-ID") String tenantId,
            @Parameter(description = "ID del servicio", required = true)
            @PathVariable Long servicioId) {
        return ResponseEntity.ok(pricingService.getPricesByService(servicioId, tenantId));
    }

    @GetMapping("/activo/{servicioId}")
    @Operation(summary = "Obtener precio activo",
            description = "Devuelve el precio actualmente activo para un servicio")
    public ResponseEntity<ServicePriceResponseDTO> obtenerPrecioActivo(
            @Parameter(description = "ID del tenant (obligatorio en header)", required = true)
            @RequestHeader("X-Tenant-ID") String tenantId,
            @Parameter(description = "ID del servicio", required = true)
            @PathVariable Long servicioId) {
        return ResponseEntity.ok(pricingService.getActivePrice(servicioId, tenantId));
    }

    @GetMapping("/{servicioId}/activo")
    @Operation(summary = "Obtener precio activo (sin validación de tenant)",
            description = "Devuelve el precio activo para un servicio sin validar el tenant (uso interno)")
    public ResponseEntity<ServicePriceResponseDTO> obtenerPrecioActivoPorServicioId(
            @Parameter(description = "ID del servicio", required = true)
            @PathVariable Long servicioId) {
        return ResponseEntity.ok(pricingService.getActivePriceByServiceId(servicioId));
    }

    @GetMapping("/{servicioId}/historico")
    @Operation(summary = "Obtener histórico de precios",
            description = "Devuelve los precios de un servicio dentro de un rango de fechas")
    public ResponseEntity<List<ServicePriceResponseDTO>> obtenerHistoricoPrecios(
            @Parameter(description = "ID del tenant (obligatorio en header)", required = true)
            @RequestHeader("X-Tenant-ID") String tenantId,
            @Parameter(description = "ID del servicio", required = true)
            @PathVariable Long servicioId,
            @Parameter(description = "Fecha de inicio (opcional, formato: yyyy-MM-dd'T'HH:mm:ss)")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @Parameter(description = "Fecha de fin (opcional, formato: yyyy-MM-dd'T'HH:mm:ss)")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        return ResponseEntity.ok(pricingService.getHistoricalPrices(servicioId, tenantId, fechaInicio, fechaFin));
    }

    @PutMapping("/{precioId}")
    @Operation(summary = "Actualizar precio",
            description = "Actualiza un precio existente manteniendo el historial")
    public ResponseEntity<ServicePriceResponseDTO> actualizarPrecio(
            @Parameter(description = "ID del tenant (obligatorio en header)", required = true)
            @RequestHeader("X-Tenant-ID") String tenantId,
            @Parameter(description = "ID del precio", required = true)
            @PathVariable Long precioId,
            @RequestBody ServicePriceRequestDTO dto) {
        return ResponseEntity.ok(pricingService.updatePrice(precioId, tenantId, dto));
    }

    @DeleteMapping("/{precioId}")
    @Operation(summary = "Eliminar precio",
            description = "Elimina permanentemente un precio del sistema")
    public ResponseEntity<Void> eliminarPrecio(
            @Parameter(description = "ID del tenant (obligatorio en header)", required = true)
            @RequestHeader("X-Tenant-ID") String tenantId,
            @Parameter(description = "ID del precio", required = true)
            @PathVariable Long precioId) {
        pricingService.deletePrice(precioId, tenantId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{precioId}/desactivar")
    @Operation(summary = "Desactivar precio",
            description = "Desactiva un precio estableciendo la fecha de fin a ahora")
    public ResponseEntity<ServicePriceResponseDTO> desactivarPrecio(
            @Parameter(description = "ID del tenant (obligatorio en header)", required = true)
            @RequestHeader("X-Tenant-ID") String tenantId,
            @Parameter(description = "ID del precio", required = true)
            @PathVariable Long precioId) {
        return ResponseEntity.ok(pricingService.deactivatePrice(precioId, tenantId));
    }

    @GetMapping("/{servicioId}/verificar-superposicion")
    @Operation(summary = "Verificar superposición de precios",
            description = "Verifica si un nuevo precio se superpondría con precios existentes")
    public ResponseEntity<Boolean> verificarSuperposicionPrecio(
            @Parameter(description = "ID del servicio", required = true)
            @PathVariable Long servicioId,
            @Parameter(description = "Fecha de inicio del nuevo precio", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @Parameter(description = "Fecha de fin del nuevo precio (opcional)")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        return ResponseEntity.ok(pricingService.hasOverlappingPrice(servicioId, fechaInicio, fechaFin));
    }
}