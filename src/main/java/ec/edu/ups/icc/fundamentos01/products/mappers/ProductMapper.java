package ec.edu.ups.icc.fundamentos01.products.mappers;

import ec.edu.ups.icc.fundamentos01.products.models.Product;
import ec.edu.ups.icc.fundamentos01.products.dtos.ProductResponseDto;

public class ProductMapper {

    public static Product toEntity(Long id, String name, String description, double price, int stock) {
        Product product = new Product(name, price, description);
        product.setId(id);
        product.setStock(stock);
        return product;
    }

    public static ProductResponseDto toResponse(Product product) {
        ProductResponseDto dto = new ProductResponseDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        return dto;
    }
}
