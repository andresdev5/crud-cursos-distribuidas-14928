package ec.edu.espe.msvc.users.services;

import ec.edu.espe.msvc.users.client.CourseServiceClient;
import ec.edu.espe.msvc.users.dto.EnrollmentDto;
import ec.edu.espe.msvc.users.entity.User;
import ec.edu.espe.msvc.users.dto.UserDto;
import ec.edu.espe.msvc.users.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository repository;

    @Autowired
    private CourseServiceClient courseServiceClient;

    @Override
    public List<User> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<User> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public User save(UserDto user) {
        User entity = User.builder()
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();

        return repository.save(entity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El id del usuario no existe o es incorrecto.");
        }

        List<EnrollmentDto> enrollments = courseServiceClient.getEnrollmentsByUserId(id);

        if (!enrollments.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario tiene cursos registrados, por favor elimine las matrículas asociadas antes de eliminar el usuario.");
        }

        repository.deleteById(id);
    }

    @Override
    public User update(UserDto user) {
        if (user.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El id del usuario es requerido.");
        }

        User entity = repository.findById(user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El id del usuario no existe o es incorrecto."));

        if (user.getName() != null && !user.getName().isEmpty()) {
            entity.setName(user.getName());
        }

        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            entity.setEmail(user.getEmail());
        }

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            entity.setPassword(user.getPassword());
        }

        return repository.save(entity);
    }

    @Override
    public List<UserDto> fetchUsersByIds(List<Long> ids) {
        return repository.findAllById(ids).stream().map(user -> UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build()).toList();
    }

    @Override
    public List<UserDto> search(String query) {
        return repository.findByNameContainingIgnoreCase(query).stream().map(user -> UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build()).toList();
    }
}
