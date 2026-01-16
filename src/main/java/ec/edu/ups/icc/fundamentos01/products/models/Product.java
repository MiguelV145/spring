package ec.edu.ups.icc.fundamentos01.products.models;

import java.time.LocalDateTime;

import ec.edu.ups.icc.fundamentos01.Categories.entity.CategoriaEntity;
import ec.edu.ups.icc.fundamentos01.products.dtos.CreateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.PartialUpdateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.UpdateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.ProductResponseDto;
import ec.edu.ups.icc.fundamentos01.products.entities.ProductEntity;
import ec.edu.ups.icc.fundamentos01.users.entities.UserEntity;

public class Product {

    // ==================== VARIABLES DE INSTANCIA ====================
    private Long id;
    private String name;
    private String description;
    private double price;
    private int stock;
    private LocalDateTime createdAt;

    // ==================== CONSTRUCTORES ====================

    public Product(){
        
    }
   
    public Product(String name, Double price, String description) {
        this.validateBusinessRules(name, price, description);
        this.name = name;
        this.price = price;
        this.description = description;
    }

    private void validateBusinessRules(String name, Double price, String description) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio");
        }
        if (price == null || price <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a 0");
        }
        if (description != null && description.length() > 500) {
            throw new IllegalArgumentException("La descripción no puede superar 500 caracteres");
        }
    }
    

    // ==================== GETTERS Y SETTERS ====================
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // ==================== FACTORY METHODS ====================


 
    /**
     * Crea un Product desde un DTO de creación
     * @param dto DTO con datos del formulario
     * @return instancia de Product para lógica de negocio
     */

    public static Product fromDto(CreateProductDto dto) {
        return new Product(dto.name, dto.price, dto.description);
    }

    /**
     * Crea un Product desde una entidad persistente
     * @param entity Entidad recuperada de la BD
     * @return instancia de Product para lógica de negocio
     */
public static Product fromEntity(ProductEntity entity) {
        Product product = new Product(
            entity.getName(), 
            entity.getPrice(), 
            entity.getDescription()
        );
        product.id = entity.getId();
        return product;
    }

    public ProductEntity toEntity(UserEntity owner, CategoriaEntity category) {
        ProductEntity entity = new ProductEntity();
        
        if (this.id != null && this.id > 0) {
            entity.setId(this.id);
        }
        
        entity.setName(this.name);
        entity.setPrice(this.price);
        entity.setDescription(this.description);
        
        // Asignar relaciones
        entity.setOwner(owner);
        entity.setCategory(category);
        
        return entity;
    }

    

    // ==================== CONVERSION METHODS ====================

    /**
     * Convierte este Product a una entidad persistente
     * @return ProductEntity lista para guardar en BD
     */
    public ProductEntity toEntity() {
        ProductEntity entity = new ProductEntity();

        // Si ya tiene id, lo asignamos (para updates)
        if (this.id != null && this.id > 0) {
            entity.setId(this.id);
        }

        entity.setName(this.name);
        entity.setDescription(this.description);
        entity.setPrice(this.price);
        entity.setStock(this.stock);
        return entity;
    }

    /**
     * Convierte este Product a un DTO de respuesta
     * @return DTO sin información sensible
     */
    public ProductResponseDto toResponseDto() {
        ProductResponseDto dto = new ProductResponseDto();
        dto.setId(this.id);
        dto.setName(this.name);
        dto.setDescription(this.description);
        dto.setPrice(this.price);
        dto.setStock(this.stock);
        if (this.createdAt != null) {
            dto.setCreatedAt(this.createdAt.toString());
        }
        return dto;
    }

    // ==================== UPDATE METHODS ====================

    /**
     * Actualiza todos los campos del producto
     * @param dto DTO con los nuevos datos
     * @return this para encadenamiento
     */
    public Product update(UpdateProductDto dto) {
        if (dto.name == null || dto.name.isBlank())
            throw new IllegalArgumentException("El nombre es obligatorio");
        if (dto.price < 0)
            throw new IllegalArgumentException("El precio no puede ser negativo");
        if (dto.stock < 0)
            throw new IllegalArgumentException("El stock no puede ser negativo");

        this.name = dto.name;
        this.description = dto.description;
        this.price = dto.price;
        this.stock = dto.stock;
        return this;
    }

    /**
     * Actualiza solo los campos proporcionados
     * @param dto DTO con los campos a actualizar (opcionales)
     * @return this para encadenamiento
     */
    public Product partialUpdate(PartialUpdateProductDto dto) {
        if (dto.name != null) {
            if (dto.name.isBlank())
                throw new IllegalArgumentException("El nombre no puede estar vacío");
            this.name = dto.name;
        }
        if (dto.description != null) {
            this.description = dto.description;
        }
        if (dto.price != null) {
            if (dto.price < 0)
                throw new IllegalArgumentException("El precio no puede ser negativo");
            this.price = dto.price;
        }
        if (dto.stock != null) {
            if (dto.stock < 0)
                throw new IllegalArgumentException("El stock no puede ser negativo");
            this.stock = dto.stock;
        }
        return this;
    }
}
