package ec.edu.ups.icc.fundamentos01.products.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ec.edu.ups.icc.fundamentos01.products.entities.ProductEntity;


@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    Optional<ProductEntity> findByName(String name);

    //// select * from products where user:id
    List<ProductEntity> findByOwnerId(Long userId);
    List<ProductEntity> findByCategoryId(Long categoryId);
    List<ProductEntity> findByOwnerName(String name);
    List<ProductEntity> findByCategoryName(String name);
    List<ProductEntity> findByCategoryIdAndPriceGreaterThan(Long categoryId, Double price);
    
    
}


