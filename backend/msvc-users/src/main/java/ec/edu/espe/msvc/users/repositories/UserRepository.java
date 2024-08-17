package ec.edu.espe.msvc.users.repositories;

import ec.edu.espe.msvc.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByNameContainingIgnoreCase(String query);
}
