package ec.edu.ups.icc.fundamentos01.users.models;

import java.time.LocalDateTime;

import ec.edu.ups.icc.fundamentos01.users.dtos.CreateUserDto;
import ec.edu.ups.icc.fundamentos01.users.dtos.PartialUpdateUserDto;
import ec.edu.ups.icc.fundamentos01.users.dtos.UpdateUserDto;
import ec.edu.ups.icc.fundamentos01.users.dtos.UserResponseDto;
import ec.edu.ups.icc.fundamentos01.users.entities.UserEntity;

public class User {

     
    private int id;
    private String name;
    private String email;
    private String password;
    private LocalDateTime createdAt;
    

    public User(int id, String name, String email, String password) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Nombre inválido");

        if (email == null || !email.contains("@"))
            throw new IllegalArgumentException("Email inválido");

        if (password == null || password.length() < 8)
            throw new IllegalArgumentException("Password inválido");

        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.createdAt = LocalDateTime.now();
    }

   
    
public int getId() {
    return id;
}

public void setId(int id) {
    this.id = id;
}

public String getName() {
    return name;
}

public void setName(String name) {
    this.name = name;
}

public String getEmail() {
    return email;
}

public void setEmail(String email) {
    this.email = email;
}

public String getPassword() {
    return password;
}

public void setPassword(String password) {
    this.password = password;
}

public LocalDateTime getCreatedAt() {
    return createdAt;
}

public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
}

    

    // ==================== FACTORY METHODS ====================

    /**
     * Crea un User desde un DTO de creación
     * @param dto DTO con datos del formulario
     * @return instancia de User para lógica de negocio
     */
    public static User fromDto(CreateUserDto dto) {
        return new User(
            0,                // id = 0 porque aún no existe en BD
            dto.getName(),
            dto.getEmail(),
            dto.getPassword()
        );
    }

    /**
     * Crea un User desde una entidad persistente
     * @param entity Entidad recuperada de la BD
     * @return instancia de User para lógica de negocio
     */
    public static User fromEntity(UserEntity entity) {
        return new User(
            entity.getId().intValue(),
            entity.getName(),
            entity.getEmail(),
            entity.getPassword()
        );
    }

    // ==================== CONVERSION METHODS ====================

    /**
     * Convierte este User a una entidad persistente
     * @return UserEntity lista para guardar en BD
     */
    public UserEntity toEntity() {
        UserEntity entity = new UserEntity();

        // Si ya tiene id, lo asignamos (para updates)
        if (this.id > 0) {
            entity.setId((long) this.id);
        }

        entity.setName(this.name);
        entity.setEmail(this.email);
        entity.setPassword(this.password);
        return entity;
    }

    /**
     * Convierte este User a un DTO de respuesta
     * @return DTO sin información sensible
     */
    public UserResponseDto toResponseDto() {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(this.id);
        dto.setName(this.name);
        dto.setEmail(this.email);
        return dto; // sin password
    }


    public User update(UpdateUserDto dto) {
        // Validaciones de reglas de negocio
        if (dto.name == null || dto.name.isBlank())
            throw new IllegalArgumentException("El nombre es obligatorio");
        
        if (dto.email == null || dto.email.isBlank())
            throw new IllegalArgumentException("El email es obligatorio");
        
        if (dto.password == null || dto.password.length() < 8)
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres");

        this.name = dto.name.trim();
        this.email = dto.email.trim().toLowerCase();
        this.password = dto.password;
        return this;
    }

    public User partialUpdate(PartialUpdateUserDto dto) {
        if (dto.name != null) {
            if (dto.name.isBlank())
                throw new IllegalArgumentException("El nombre no puede estar vacío");
            this.name = dto.name.trim();
        }
        if (dto.email != null) {
            if (dto.email.isBlank())
                throw new IllegalArgumentException("El email no puede estar vacío");
            this.email = dto.email.trim().toLowerCase();
        }
        if (dto.password != null) {
            if (dto.password.length() < 8)
                throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres");
            this.password = dto.password;
        }
        return this;
    }

}
