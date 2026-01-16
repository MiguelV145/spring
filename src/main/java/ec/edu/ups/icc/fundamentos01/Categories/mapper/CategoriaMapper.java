package ec.edu.ups.icc.fundamentos01.Categories.mapper;

import ec.edu.ups.icc.fundamentos01.Categories.dto.CategoriaResponseDto;
import ec.edu.ups.icc.fundamentos01.Categories.models.Categorie;

public class CategoriaMapper {
    public static Categorie toEntity(Long id, String name , String description){
        Categorie categorie= new Categorie(name,description);
        categorie.setId(id);
        return categorie;

    }

    public static CategoriaResponseDto toResponse(Categorie categorie){
        CategoriaResponseDto dto= new CategoriaResponseDto();
        dto.setId(categorie.getId());
        dto.setName(categorie.getName());
        dto.setDescription(categorie.getDescription());

        return dto;
    }
    
}
