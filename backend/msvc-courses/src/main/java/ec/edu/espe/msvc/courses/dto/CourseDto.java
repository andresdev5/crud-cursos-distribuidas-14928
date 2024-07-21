package ec.edu.espe.msvc.courses.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CourseDto {
    public interface Create {}
    public interface Update {}

    @NotNull(groups = Update.class, message = "El id del curso es requerido")
    private Long id;

    @Length(groups = {Create.class, Update.class}, min = 4, message = "El nombre debe tener al menos 4 caracteres")
    @NotBlank(groups = {Create.class, Update.class}, message = "El nombre es requerido")
    private String name;
}
