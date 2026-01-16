package ec.edu.ups.icc.fundamentos01.Categories.services;

import java.util.List;
import org.springframework.stereotype.Service;
import ec.edu.ups.icc.fundamentos01.Categories.dto.CategoriaResponseDto;
import ec.edu.ups.icc.fundamentos01.Categories.dto.CreateCategoriaDto;
import ec.edu.ups.icc.fundamentos01.Categories.entity.CategoriaEntity;
import ec.edu.ups.icc.fundamentos01.Categories.entity.Repository.CategoryRepository;
import ec.edu.ups.icc.fundamentos01.Categories.models.Categorie;
import ec.edu.ups.icc.fundamentos01.exception.domain.ConflictException;

@Service
public class CategoriaServicesImple implements CategoriaServices {

    private final CategoryRepository categoryRepository;

    public CategoriaServicesImple(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<CategoriaResponseDto> findAll(){

        return categoryRepository.findAll().stream().map(Categorie::fromEntity).map(Categorie::tResponseDto).toList();
    }
    
    @Override
    public void save(CreateCategoriaDto dto) {
        // Validar que el nombre no sea nulo o vacío
        if (dto.name == null || dto.name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoría es obligatorio");
        }

        // Validar que no exista una categoría con el mismo nombre
        categoryRepository.findByNameIgnoreCase(dto.name)
                .ifPresent(existingCategory -> {
                    throw new ConflictException("Ya existe una categoría con el nombre: " + dto.name);
                });

        // Crear la nueva categoría
        CategoriaEntity entity = new CategoriaEntity();
        entity.setName(dto.name);
        entity.setDescription(dto.description);

        // Guardar en la base de datos
        categoryRepository.save(entity);
    }
}
