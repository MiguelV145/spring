package ec.edu.ups.icc.fundamentos01.products.services;

import java.util.List;

import org.springframework.stereotype.Service;

import ec.edu.ups.icc.fundamentos01.Categories.dto.CategoriaResponseDto;
import ec.edu.ups.icc.fundamentos01.Categories.entity.CategoriaEntity;
import ec.edu.ups.icc.fundamentos01.Categories.entity.Repository.CategoryRepository;
import ec.edu.ups.icc.fundamentos01.exception.domain.ConflictException;
import ec.edu.ups.icc.fundamentos01.exception.domain.NotFoundException;
import ec.edu.ups.icc.fundamentos01.products.models.Product;
import ec.edu.ups.icc.fundamentos01.products.entities.ProductEntity;
import ec.edu.ups.icc.fundamentos01.products.mappers.ProductMapper;
import ec.edu.ups.icc.fundamentos01.products.dtos.CreateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.PartialUpdateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.UpdateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.ProductResponseDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.SecureUpdateProductosDto;
import ec.edu.ups.icc.fundamentos01.products.repositories.ProductRepository;
import ec.edu.ups.icc.fundamentos01.users.entities.UserEntity;
import ec.edu.ups.icc.fundamentos01.users.repositories.UserRepository;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepo;

    private final UserRepository userRepo;

    private final CategoryRepository categorieRepo;



    public ProductServiceImpl(ProductRepository productRepo, UserRepository userRepo,
            CategoryRepository categorieRepo) {
        this.productRepo = productRepo;
        this.userRepo = userRepo;
        this.categorieRepo = categorieRepo;
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
        UserEntity owner = userRepo.findById(dto.userId)
            .orElseThrow(() -> new NotFoundException("usuario no existe"));

        CategoriaEntity categoria = categorieRepo.findById(dto.categoryId)
            .orElseThrow(() -> new NotFoundException("categoria no existe"));
        //Convierte DTO-> Domain
        Product newProduct= Product.fromDto(dto);

        ProductEntity entity= newProduct.toEntity(owner, categoria);
        //Persistir
        ProductEntity saved =productRepo.save(entity);

        return toResponseDto(saved);
    }

    private ProductResponseDto toResponseDto( ProductEntity entity){
        ProductResponseDto dto= new ProductResponseDto();
        dto.id= entity.getId();
        dto.name=entity.getName();
        dto.price= entity.getPrice();
        dto.description= entity.getDescription();

        ProductResponseDto.UserSummaryDto ownerDto= new ProductResponseDto.UserSummaryDto();
        ownerDto.id = entity.getOwner().getId().intValue();
        ownerDto.name =entity.getOwner().getName();


        CategoriaResponseDto categoriaDto = new CategoriaResponseDto();
        categoriaDto.id= entity.getCategory().getId();
        categoriaDto.name = entity.getCategory().getName();
        categoriaDto.description = entity.getCategory().getDescription();
        dto.category = categoriaDto;
        
        return dto;
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
    

    @Override
    public Boolean validateName( Integer id , String name ){
        productRepo.findByName(name)
        .ifPresent(exitencia ->{
            if (id == null || exitencia.getId() != id.longValue()){
                throw new ConflictException("ya existe un producto con el nombre de : "+name);
            }
        });
        return true;

    }


    @Override
    public ProductResponseDto secureUpdate(int id, SecureUpdateProductosDto dto){
        ProductEntity entity = productRepo.findById((long)id)
            .orElseThrow(() -> new NotFoundException("El producto no existe"));

        if (dto.price != null && dto.price > 1000){
            if (dto.reason == null || dto.reason.isBlank()){
                throw new ConflictException("Productos con precio mayor a 1000 requieren justificación");  
            }
        }

        Product producto = Product.fromEntity(entity);
        if(dto.name != null) producto.setName(dto.name);
        if(dto.price != null) producto.setPrice(dto.price);
        if(dto.description != null) producto.setDescription(dto.description);

        ProductEntity saved = productRepo.save(producto.toEntity());

        return ProductMapper.toResponse(Product.fromEntity(saved));
    }


    
}
