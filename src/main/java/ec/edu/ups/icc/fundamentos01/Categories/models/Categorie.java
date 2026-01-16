package ec.edu.ups.icc.fundamentos01.Categories.models;

import ec.edu.ups.icc.fundamentos01.Categories.dto.CategoriaResponseDto;
import ec.edu.ups.icc.fundamentos01.Categories.dto.CreateCategoriaDto;
import ec.edu.ups.icc.fundamentos01.Categories.entity.CategoriaEntity;

public class Categorie {

    private Long id;
    private String name;
    private String description;

    public Categorie() {

    }

    public Categorie(String name, String description){
        this.name=name;
        this.description=description;
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
    
// Metodos Factory paras la factorias jajaj
    public static Categorie fromCategorie(CreateCategoriaDto dto){
        return new Categorie(dto.name, dto.description);
    }

    public static Categorie fromEntity( CategoriaEntity entity){
        Categorie categoria= new Categorie(entity.getName(), entity.getDescription());
        categoria.id = entity.getId();
        return categoria;
    }


    public CategoriaEntity toEntity(){
        CategoriaEntity entity= new CategoriaEntity();

        if (this.id != null && this.id>0){
            entity.setId(id);
        }
        entity.setName(name);
        entity.setDescription(description);

        return entity;
    }


    public CategoriaResponseDto tResponseDto(){
        CategoriaResponseDto dto = new CategoriaResponseDto();
        dto.setId(this.id);
        dto.setName(this.name);
        dto.setDescription(this.description);

        return dto;
    }

}
