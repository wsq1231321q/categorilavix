package com.lavanderia.productservice.controller;

import com.lavanderia.productservice.dto.request.ServiceCategoryRequestDTO;
import com.lavanderia.productservice.dto.response.ServiceCategoryResponseDTO;
import com.lavanderia.productservice.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@Tag(name = "Gestión de Categorías", description = "APIs para administrar categorías de servicios de lavandería")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    @Operation(summary = "Crear una nueva categoría",
            description = "Crea una nueva categoría de servicios para un tenant específico")
    public ResponseEntity<ServiceCategoryResponseDTO> crearCategoria(
            @Parameter(description = "ID del tenant (obligatorio en header)", required = true)
            @RequestHeader("X-Tenant-ID") String tenantId,
            @RequestBody ServiceCategoryRequestDTO dto) {
        return new ResponseEntity<>(categoryService.createCategory(tenantId, dto), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Obtener todas las categorías",
            description = "Devuelve todas las categorías del sistema (solo para administración)")
    public ResponseEntity<List<ServiceCategoryResponseDTO>> obtenerTodasCategorias() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener categoría por ID",
            description = "Devuelve una categoría específica verificando que pertenezca al tenant")
    public ResponseEntity<ServiceCategoryResponseDTO> obtenerCategoriaPorId(
            @Parameter(description = "ID del tenant (obligatorio en header)", required = true)
            @RequestHeader("X-Tenant-ID") String tenantId,
            @Parameter(description = "ID de la categoría", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id, tenantId));
    }

    @GetMapping("/por-tenant/{tenantId}")
    @Operation(summary = "Obtener categorías por tenant",
            description = "Devuelve todas las categorías asociadas a un tenant específico")
    public ResponseEntity<List<ServiceCategoryResponseDTO>> obtenerCategoriasPorTenant(
            @Parameter(description = "ID del tenant", required = true)
            @PathVariable String tenantId) {
        return ResponseEntity.ok(categoryService.getAllCategoriesByTenant(tenantId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar categoría",
            description = "Actualiza los datos de una categoría existente")
    public ResponseEntity<ServiceCategoryResponseDTO> actualizarCategoria(
            @Parameter(description = "ID del tenant (obligatorio en header)", required = true)
            @RequestHeader("X-Tenant-ID") String tenantId,
            @Parameter(description = "ID de la categoría", required = true)
            @PathVariable Long id,
            @RequestBody ServiceCategoryRequestDTO dto) {
        return ResponseEntity.ok(categoryService.updateCategory(id, tenantId, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar categoría",
            description = "Elimina una categoría si no tiene servicios asociados")
    public ResponseEntity<Void> eliminarCategoria(
            @Parameter(description = "ID del tenant (obligatorio en header)", required = true)
            @RequestHeader("X-Tenant-ID") String tenantId,
            @Parameter(description = "ID de la categoría", required = true)
            @PathVariable Long id) {
        categoryService.deleteCategory(id, tenantId);
        return ResponseEntity.noContent().build();
    }
}