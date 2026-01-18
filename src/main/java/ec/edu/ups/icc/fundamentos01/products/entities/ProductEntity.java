package ec.edu.ups.icc.fundamentos01.products.entities;

import java.util.Set;

import ec.edu.ups.icc.fundamentos01.Categories.entity.CategoriaEntity;
import ec.edu.ups.icc.fundamentos01.core.entities.BaseModel;
import ec.edu.ups.icc.fundamentos01.users.entities.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "products")
public class ProductEntity extends BaseModel {
    
    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private int stock;



    //Atributos relacionales
    /// Con usuarios donde un usuario puede tener muchos productos 
    /// 
    
     @ManyToOne(optional = false, fetch= FetchType.LAZY)
     @JoinColumn(name = "user_id", nullable = false)
     private UserEntity owner;

    // @ManyToOne(optional = false, fetch = FetchType.LAZY)
    // @JoinColumn(name = "category_id", nullable = false)
    // private CategoriaEntity category;

    public UserEntity getOwner() {
        return owner;
    }

     public void setOwner(UserEntity owner) {
         this.owner = owner;
     }
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name= "product_categories", 
        joinColumns = @JoinColumn(name= "product_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
     )//tabla intermedia)
     private Set<CategoriaEntity> categories;

    // Getters
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public double getPrice() {
        return price;
    }
    
    public int getStock() {
        return stock;
    }
    
    // Setters
    public void setName(String name) {
        this.name = name;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public void setStock(int stock) {
        this.stock = stock;
    }

    public Set<CategoriaEntity> getCategories() {
        return categories;
    }

    public void setCategories(Set<CategoriaEntity> categories) {
        this.categories = categories;
    }

    public void addCategorie(CategoriaEntity categoriaEntity){
        this.categories.add(categoriaEntity);
    }
    public void removeCategorie(CategoriaEntity categoriaEntity){
        this.categories.remove(categoriaEntity);
    }
    public void clearCategorie(CategoriaEntity categoriaEntity){
        this.categories.clear();
    }
}
