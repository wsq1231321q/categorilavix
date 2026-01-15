package com.lavanderia.productservice.controller;

import com.lavanderia.productservice.dto.request.LaundryServiceRequestDTO;
import com.lavanderia.productservice.dto.response.LaundryServiceResponseDTO;
import com.lavanderia.productservice.service.LaundryServiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servicios")
@Tag(name = "Gestión de Servicios", description = "APIs para administrar servicios de lavandería")
public class LaundryServiceController {

    private final LaundryServiceService laundryServiceService;

    @Autowired
    public LaundryServiceController(LaundryServiceService laundryServiceService) {
        this.laundryServiceService = laundryServiceService;
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo servicio",
            description = "Crea un nuevo servicio de lavandería en una categoría específica")
    public ResponseEntity<LaundryServiceResponseDTO> crearServicio(
            @Parameter(description = "ID del tenant (obligatorio en header)", required = true)
            @RequestHeader("X-Tenant-ID") String tenantId,
            @RequestBody LaundryServiceRequestDTO dto) {
        return new ResponseEntity<>(laundryServiceService.createService(tenantId, dto), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Obtener todos los servicios",
            description = "Devuelve todos los servicios del sistema (solo para administración)")
    public ResponseEntity<List<LaundryServiceResponseDTO>> obtenerTodosServicios() {
        return ResponseEntity.ok(laundryServiceService.getAllServices());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener servicio por ID",
            description = "Devuelve un servicio específico verificando que pertenezca al tenant")
    public ResponseEntity<LaundryServiceResponseDTO> obtenerServicioPorId(
            @Parameter(description = "ID del tenant (obligatorio en header)", required = true)
            @RequestHeader("X-Tenant-ID") String tenantId,
            @Parameter(description = "ID del servicio", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(laundryServiceService.getServiceById(id, tenantId));
    }

    @GetMapping("/por-categoria/{categoriaId}")
    @Operation(summary = "Obtener servicios por categoría",
            description = "Devuelve todos los servicios de una categoría específica")
    public ResponseEntity<List<LaundryServiceResponseDTO>> obtenerServiciosPorCategoria(
            @Parameter(description = "ID del tenant (obligatorio en header)", required = true)
            @RequestHeader("X-Tenant-ID") String tenantId,
            @Parameter(description = "ID de la categoría", required = true)
            @PathVariable Long categoriaId) {
        return ResponseEntity.ok(laundryServiceService.getServicesByCategory(categoriaId, tenantId));
    }

    @GetMapping("/por-tenant/{tenantId}")
    @Operation(summary = "Obtener servicios por tenant",
            description = "Devuelve todos los servicios asociados a un tenant específico")
    public ResponseEntity<List<LaundryServiceResponseDTO>> obtenerServiciosPorTenant(
            @Parameter(description = "ID del tenant", required = true)
            @PathVariable String tenantId) {
        return ResponseEntity.ok(laundryServiceService.getAllServicesByTenant(tenantId));
    }

    @GetMapping("/delicados/{tenantId}")
    @Operation(summary = "Obtener servicios delicados",
            description = "Devuelve todos los servicios marcados como delicados de un tenant")
    public ResponseEntity<List<LaundryServiceResponseDTO>> obtenerServiciosDelicados(
            @Parameter(description = "ID del tenant", required = true)
            @PathVariable String tenantId) {
        return ResponseEntity.ok(laundryServiceService.getDelicateServicesByTenant(tenantId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar servicio",
            description = "Actualiza los datos de un servicio existente")
    public ResponseEntity<LaundryServiceResponseDTO> actualizarServicio(
            @Parameter(description = "ID del tenant (obligatorio en header)", required = true)
            @RequestHeader("X-Tenant-ID") String tenantId,
            @Parameter(description = "ID del servicio", required = true)
            @PathVariable Long id,
            @RequestBody LaundryServiceRequestDTO dto) {
        return ResponseEntity.ok(laundryServiceService.updateService(id, tenantId, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar servicio",
            description = "Elimina un servicio si no tiene precios o reglas asociadas")
    public ResponseEntity<Void> eliminarServicio(
            @Parameter(description = "ID del tenant (obligatorio en header)", required = true)
            @RequestHeader("X-Tenant-ID") String tenantId,
            @Parameter(description = "ID del servicio", required = true)
            @PathVariable Long id) {
        laundryServiceService.deleteService(id, tenantId);
        return ResponseEntity.noContent().build();
    }
}