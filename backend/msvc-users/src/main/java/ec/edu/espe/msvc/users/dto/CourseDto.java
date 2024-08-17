package ec.edu.espe.msvc.users.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseDto {
    public interface Create {}
    public interface Update {}

    @NotNull(groups = Update.class, message = "El id del curso es requerido")
    private Long id;

    @Length(groups = {Create.class, Update.class}, min = 4, message = "El nombre debe tener al menos 4 caracteres")
    @NotBlank(groups = {Create.class, Update.class}, message = "El nombre es requerido")
    private String name;

    private String description;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    @Builder.Default
    private List<EnrollmentDto> enrollments = new ArrayList<>();
}
