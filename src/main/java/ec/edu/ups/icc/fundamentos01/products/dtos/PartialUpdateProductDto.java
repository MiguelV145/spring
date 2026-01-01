package ec.edu.ups.icc.fundamentos01.products.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public class PartialUpdateProductDto {
    @Size(min = 3, max = 150, message = "El nombre debe tener entre 3 y 150 caracteres")
    public String name;
    
    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    public String description;
    
    @Min(value = 0, message = "El precio no puede ser negativo")
    public Double price;
    
    @Min(value = 0, message = "El stock no puede ser negativo")
    public Integer stock;
    
    // Getters
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public Double getPrice() {
        return price;
    }
    
    public Integer getStock() {
        return stock;
    }
    
    // Setters
    public void setName(String name) {
        this.name = name;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public void setPrice(Double price) {
        this.price = price;
    }
    
    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
