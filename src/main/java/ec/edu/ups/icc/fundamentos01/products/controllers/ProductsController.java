package ec.edu.ups.icc.fundamentos01.products.controllers;

import ec.edu.ups.icc.fundamentos01.products.dtos.CreateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.UpdateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.PartialUpdateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.ProductResponseDto;
import ec.edu.ups.icc.fundamentos01.products.entities.Product;
import ec.edu.ups.icc.fundamentos01.products.mappers.ProductMapper;
import ec.edu.ups.icc.fundamentos01.products.services.ProductService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductsController {
   
	private final ProductService service;

	public ProductsController(ProductService service) {
		this.service = service;
	}

	@GetMapping
	public List<ProductResponseDto> findAll() {
		return service.findAll();
	}

	@GetMapping("/{id}")
	public ProductResponseDto findOne(@PathVariable("id") int id) {
		return service.findOne(id);
	}

	@PostMapping
	public ProductResponseDto create(@RequestBody CreateProductDto dto) {
		return service.create(dto);
	}

	@PutMapping("/{id}")
	public Object update(@PathVariable("id") int id,
	                     @RequestBody UpdateProductDto dto) {
		return service.update(id, dto);
	}

	@PatchMapping("/{id}")
	public Object partialUpdate(@PathVariable("id") int id,
	                            @RequestBody PartialUpdateProductDto dto) {
		return service.partialUpdate(id, dto);
	}

	@DeleteMapping("/{id}")
	public Object delete(@PathVariable("id") int id) {
		return service.delete(id);
	}
}
