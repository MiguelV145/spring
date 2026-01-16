package ec.edu.ups.icc.fundamentos01.Categories.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ec.edu.ups.icc.fundamentos01.Categories.dto.CategoriaResponseDto;
import ec.edu.ups.icc.fundamentos01.Categories.dto.CreateCategoriaDto;
import ec.edu.ups.icc.fundamentos01.Categories.services.CategoriaServices;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("api/categories")
public class CategoriaController {

    private CategoriaServices categoriaServices;

    public CategoriaController(CategoriaServices categoriaServices) {
        this.categoriaServices = categoriaServices;
    }

    @GetMapping
    public List<CategoriaResponseDto> findAll() {
        return categoriaServices.findAll();

    }
    

    @PostMapping
    public ResponseEntity<String> save(@RequestBody CreateCategoriaDto dto) {
        categoriaServices.save(dto);
        return ResponseEntity.ok("Categoria Creada");
    }
    



    
}
