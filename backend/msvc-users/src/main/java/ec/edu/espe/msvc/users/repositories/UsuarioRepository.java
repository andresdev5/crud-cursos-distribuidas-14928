package ec.edu.espe.msvc.users.repositories;

import ec.edu.espe.msvc.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<User, Long> {}
