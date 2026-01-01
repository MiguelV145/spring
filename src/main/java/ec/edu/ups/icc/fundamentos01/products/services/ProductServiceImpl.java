package ec.edu.ups.icc.fundamentos01.products.services;

import java.util.List;

import org.springframework.stereotype.Service;

import ec.edu.ups.icc.fundamentos01.exception.domain.ConflictException;
import ec.edu.ups.icc.fundamentos01.exception.domain.NotFoundException;
import ec.edu.ups.icc.fundamentos01.products.models.Product;
import ec.edu.ups.icc.fundamentos01.products.entities.ProductEntity;
import ec.edu.ups.icc.fundamentos01.products.dtos.CreateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.PartialUpdateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.UpdateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.ProductResponseDto;
import ec.edu.ups.icc.fundamentos01.products.repositories.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepo;

    public ProductServiceImpl(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    @Override
    public List<ProductResponseDto> findAll() {
        return productRepo.findAll()
                .stream()
                .map(Product::fromEntity)
                .map(Product::toResponseDto)
                .toList();
    }

    @Override
    public ProductResponseDto findOne(int id) {
        return productRepo.findById((long) id)
                .map(Product::fromEntity)
                .map(Product::toResponseDto)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado"));
    }

    @Override
    public ProductResponseDto create(CreateProductDto dto) {
        // Regla de negocio: nombre único
        if (productRepo.findByName(dto.getName()).isPresent()) {
            throw new ConflictException("Ya existe un producto con el nombre: " + dto.getName());
        }

        Product product = Product.fromDto(dto);
        ProductEntity saved = productRepo.save(product.toEntity());
        return Product.fromEntity(saved).toResponseDto();
    }

    @Override
    public ProductResponseDto update(int id, UpdateProductDto dto) {
        return productRepo.findById((long) id)
                .map(Product::fromEntity)
                .map(product -> product.update(dto))
                .map(Product::toEntity)
                .map(productRepo::save)
                .map(Product::fromEntity)
                .map(Product::toResponseDto)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado"));
    }

    @Override
    public ProductResponseDto partialUpdate(int id, PartialUpdateProductDto dto) {
        return productRepo.findById((long) id)
                .map(Product::fromEntity)
                .map(product -> product.partialUpdate(dto))
                .map(Product::toEntity)
                .map(productRepo::save)
                .map(Product::fromEntity)
                .map(Product::toResponseDto)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado"));
    }

    @Override
    public void delete(int id) {
        // Verifica existencia y elimina usando patrón funcional
        productRepo.findById((long) id)
            .ifPresentOrElse(
                productRepo::delete,
                () -> {
                    throw new NotFoundException("Producto no encontrado");
                }
            );
    }
}
