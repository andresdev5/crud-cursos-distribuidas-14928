package ec.edu.espe.msvc.users.services;

import ec.edu.espe.msvc.users.entity.User;
import ec.edu.espe.msvc.users.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAll();
    Optional<User> getById(Long id);
    User save(UserDto user);
    void delete(Long id);
    User update(UserDto user);
    List<UserDto> fetchUsersByIds(List<Long> ids);
}
