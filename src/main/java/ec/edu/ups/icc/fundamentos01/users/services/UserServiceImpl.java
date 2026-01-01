package ec.edu.ups.icc.fundamentos01.users.services;

import java.util.List;

import org.springframework.stereotype.Service;

import ec.edu.ups.icc.fundamentos01.exception.domain.ConflictException;
import ec.edu.ups.icc.fundamentos01.exception.domain.NotFoundException;
import ec.edu.ups.icc.fundamentos01.users.dtos.CreateUserDto;
import ec.edu.ups.icc.fundamentos01.users.dtos.PartialUpdateUserDto;
import ec.edu.ups.icc.fundamentos01.users.dtos.UpdateUserDto;
import ec.edu.ups.icc.fundamentos01.users.dtos.UserResponseDto;
import ec.edu.ups.icc.fundamentos01.users.entities.UserEntity;
import ec.edu.ups.icc.fundamentos01.users.mappers.UserMapper;
import ec.edu.ups.icc.fundamentos01.users.models.User;
import ec.edu.ups.icc.fundamentos01.users.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;

    public UserServiceImpl(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public List<UserResponseDto> findAll() {
        return userRepo.findAll()
            .stream()
            .map(User::fromEntity)
            .map(UserMapper::toResponse)
            .toList();
    }

    @Override
    public UserResponseDto findOne(int id) {
        return userRepo.findById((long) id)
                .map(User::fromEntity)
                .map(UserMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
    }

    @Override
    public UserResponseDto create(CreateUserDto dto) {

        // Regla: email único
        if (userRepo.findByEmail(dto.email).isPresent()) {
            throw new ConflictException("El email ya está registrado");
        }

        User user = User.fromDto(dto);

        UserEntity saved = userRepo.save(user.toEntity());

        return UserMapper.toResponse(User.fromEntity(saved));
    }

    @Override
    public UserResponseDto update(int id, UpdateUserDto dto) {
        return userRepo.findById((long) id)
            .map(User::fromEntity)
            .map(user -> user.update(dto))
            .map(User::toEntity)
            .map(userRepo::save)
            .map(User::fromEntity)
            .map(UserMapper::toResponse)
            .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
    }

    @Override
    public UserResponseDto partialUpdate(int id, PartialUpdateUserDto dto) {
        return userRepo.findById((long) id)
            .map(User::fromEntity)
            .map(user -> user.partialUpdate(dto))
            .map(User::toEntity)
            .map(userRepo::save)
            .map(User::fromEntity)
            .map(UserMapper::toResponse)
            .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
    }

    @Override
    public void delete(int id) {
        userRepo.findById((long) id)
            .ifPresentOrElse(userRepo::delete,
                () -> { throw new NotFoundException("Usuario no encontrado"); });
    }
}