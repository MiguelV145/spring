package ec.edu.ups.icc.fundamentos01.users.controllers;

import ec.edu.ups.icc.fundamentos01.users.dtos.CreateUserDto;
import ec.edu.ups.icc.fundamentos01.users.dtos.UpdateUserDto;
import ec.edu.ups.icc.fundamentos01.users.dtos.PartialUpdateUserDto;
import ec.edu.ups.icc.fundamentos01.users.dtos.UserResponseDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.ProductResponseDto;

import ec.edu.ups.icc.fundamentos01.users.services.UserService;

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
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/users")
public class UsersController {

    private final UserService service;

    public UsersController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public List<UserResponseDto> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public UserResponseDto findOne(@PathVariable("id") int id) {
        return service.findOne(id);
    }

    @PostMapping
    public UserResponseDto create(@Valid @RequestBody CreateUserDto dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public UserResponseDto update(@PathVariable("id") int id,
                         @Valid @RequestBody UpdateUserDto dto) {
        return service.update(id, dto);
    }

    @PatchMapping("/{id}")
    public UserResponseDto partialUpdate(@PathVariable("id") int id,
                                @Valid @RequestBody PartialUpdateUserDto dto) {
        return service.partialUpdate(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") int id) {
        service.delete(id);
    }

    @GetMapping("/{id}/products")
    public List<ProductResponseDto> getProductsByUserId(@PathVariable("id") Long id) {
        return service.getProdutsByUserId(id);
    }
    

    @GetMapping("/{id}/products-v2")
    public String getMethodName(@PathVariable("id") Long id,
            @RequestParam(required = false)String name,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Long categoryId) {
        return new String();
    }
    

   
}