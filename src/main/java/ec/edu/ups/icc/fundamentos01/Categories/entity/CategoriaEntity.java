package ec.edu.ups.icc.fundamentos01.Categories.entity;

import ec.edu.ups.icc.fundamentos01.core.entities.BaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name= "categories")
public class CategoriaEntity extends BaseModel{
    @Column(nullable = false, length = 100, unique = true)
    private String name;
    
    @Column(length = 500)
    private String description;

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
    
}
