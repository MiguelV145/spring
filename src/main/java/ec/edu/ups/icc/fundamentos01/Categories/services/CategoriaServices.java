package ec.edu.ups.icc.fundamentos01.Categories.services;

import java.util.List;

import org.springframework.stereotype.Service;

import ec.edu.ups.icc.fundamentos01.Categories.dto.CategoriaResponseDto;
import ec.edu.ups.icc.fundamentos01.Categories.dto.CreateCategoriaDto;

@Service
public interface  CategoriaServices {

    List<CategoriaResponseDto> findAll();
    void save(CreateCategoriaDto dto);
}
