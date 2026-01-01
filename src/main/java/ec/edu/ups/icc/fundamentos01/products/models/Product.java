package ec.edu.ups.icc.fundamentos01.products.models;

import java.time.LocalDateTime;

import ec.edu.ups.icc.fundamentos01.products.dtos.CreateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.PartialUpdateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.UpdateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.ProductResponseDto;
import ec.edu.ups.icc.fundamentos01.products.entities.ProductEntity;

public class Product {

    // ==================== VARIABLES DE INSTANCIA ====================
    private int id;
    private String name;
    private String description;
    private double price;
    private int stock;
    private LocalDateTime createdAt;

    // ==================== CONSTRUCTORES ====================
    public Product() {}

    public Product(int id, String name, String description, double price, int stock) {
        // Validaciones de reglas de negocio
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("El nombre es obligatorio");

        if (name.length() < 3 || name.length() > 150)
            throw new IllegalArgumentException("El nombre debe tener entre 3 y 150 caracteres");

        if (price < 0)
            throw new IllegalArgumentException("El precio no puede ser negativo");

        if (stock < 0)
            throw new IllegalArgumentException("El stock no puede ser negativo");

        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.createdAt = LocalDateTime.now();
    }

    // ==================== GETTERS Y SETTERS ====================
    public int getId() {
        return id;
    }

    public void setId(int id) {
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
        return new Product(
            0,                // id = 0 porque aún no existe en BD
            dto.getName(),
            dto.getDescription(),
            dto.getPrice(),
            dto.getStock()
        );
    }

    /**
     * Crea un Product desde una entidad persistente
     * @param entity Entidad recuperada de la BD
     * @return instancia de Product para lógica de negocio
     */
    public static Product fromEntity(ProductEntity entity) {
        Product product = new Product(
            entity.getId().intValue(),
            entity.getName(),
            entity.getDescription(),
            entity.getPrice(),
            entity.getStock()
        );
        if (entity.getCreatedAt() != null) {
            product.setCreatedAt(entity.getCreatedAt());
        }
        return product;
    }

    // ==================== CONVERSION METHODS ====================

    /**
     * Convierte este Product a una entidad persistente
     * @return ProductEntity lista para guardar en BD
     */
    public ProductEntity toEntity() {
        ProductEntity entity = new ProductEntity();

        // Si ya tiene id, lo asignamos (para updates)
        if (this.id > 0) {
            entity.setId((long) this.id);
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
