package ec.edu.ups.icc.fundamentos01.products.dtos;

import java.util.List;

import ec.edu.ups.icc.fundamentos01.Categories.dto.CategoriaResponseDto;

public class ProductResponseDto {
    public Long id;
    public String name;
    public String description;
    public double price;
    public int stock;
    public String reason;
    public String createdAt;
    
    public UserSummaryDto user;
    
    public CategoriaResponseDto category;

    public List<CategoriaResponseDto> categories;

        
    public static class UserSummaryDto {
        public int id;
        public String name;
        public String email;
    }

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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }


    public CategoriaResponseDto getCategory() {
        return category;
    }

    public void setCategory(CategoriaResponseDto category) {
        this.category = category;
    }

    public UserSummaryDto getUser() {
        return user;
    }

    public void setUser(UserSummaryDto user) {
        this.user = user;
    }
   
    
}
