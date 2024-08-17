package ec.edu.espe.msvc.courses.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import ec.edu.espe.msvc.courses.entity.Enrollment;
import ec.edu.espe.msvc.courses.entity.EnrollmentStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EnrollmentDto {
    public interface Create {}
    public interface Update {}
    public interface Register {}

    @NotNull(groups = Update.class, message = "El id es requerido")
    private Long id;

    @NotNull(groups = { Update.class, Register.class }, message = "El id del curso es requerido")
    private Long courseId;

    @NotNull(groups = { Update.class, Register.class }, message = "El id del usuario es requerido")
    private Long userId;

    @NotNull(groups = { Update.class, Create.class }, message = "El estado es requerido")
    @Enumerated(EnumType.STRING)
    private EnrollmentStatus status;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date enrolledAt;

    private UserDto user;
    private CourseDto course;
}
