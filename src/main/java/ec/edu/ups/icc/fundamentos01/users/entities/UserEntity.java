package ec.edu.ups.icc.fundamentos01.users.entities;

import ec.edu.ups.icc.fundamentos01.core.entities.BaseModel;
import ec.edu.ups.icc.fundamentos01.products.entities.ProductEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "users")
public class UserEntity extends BaseModel {
    
    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private Set<ProductEntity> products;

    
    // Getters
    public String getName() {
        return name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getPassword() {
        return password;
    }
    
    // Setters
    public void setName(String name) {
        this.name = name;
    }
    
    public void setEmail(String email) {
        if (email == null) {
            this.email = null;
            return;
        }
        this.email = email.trim().toLowerCase();
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    public Set<ProductEntity> getProducts() {
        return products;
    }

    public void setProducts(Set<ProductEntity> products) {
        this.products = products;
    }


    public void addProducto (ProductEntity product){
        this.products.add(product);
    }

    public void removeProducto(ProductEntity product){
        this.products.remove(product);
    }

    public void clearProducto(ProductEntity product){
        this.products.clear();
    }
}
