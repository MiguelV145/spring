package ec.edu.ups.icc.fundamentos01.users.dtos;

import ec.edu.ups.icc.fundamentos01.products.dtos.ProductResponseDto;

public class UserResponseDto {
    public Long id;
    public String name;
    public String email;
    
    public ProductResponseDto product;
    
    public String createdAt;
    
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
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    public ProductResponseDto getProduct() {
        return product;
    }
    public void setProduct(ProductResponseDto product) {
        this.product = product;
    }
    
}
