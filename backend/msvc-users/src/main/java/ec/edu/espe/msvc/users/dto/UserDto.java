package ec.edu.espe.msvc.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    public interface Update {}
    public interface Create {}

    @NotNull(groups = Update.class, message = "El id del usuario es requerido")
    private Long id;

    @Length(groups = {Create.class, Update.class}, min = 4, message = "El nombre debe tener al menos 4 caracteres")
    @NotNull(groups = {Create.class, Update.class}, message = "El email es requerido")
    private String name;

    @Email(groups = {Create.class, Update.class}, message = "El email debe ser válido")
    @NotNull(groups = {Create.class, Update.class}, message = "El email es requerido")
    private String email;

    @Length(groups = {Create.class, Update.class}, min = 4, message = "La contraseña debe tener al menos 4 caracteres")
    @NotNull(groups = {Create.class, Update.class}, message = "El email es requerido")
    private String password;
}
