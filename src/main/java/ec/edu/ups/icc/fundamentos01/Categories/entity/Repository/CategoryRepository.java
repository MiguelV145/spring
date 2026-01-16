package ec.edu.ups.icc.fundamentos01.Categories.entity.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ec.edu.ups.icc.fundamentos01.Categories.entity.CategoriaEntity;

public interface CategoryRepository extends JpaRepository<CategoriaEntity, Long>{
    
    boolean existsByName(String name);

    /**
     * Busca categoría por nombre (case insensitive)
     */
    Optional<CategoriaEntity> findByNameIgnoreCase(String name); 

}
