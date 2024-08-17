package ec.edu.espe.msvc.users.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    private Long id;

    private Long courseId;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private EnrollmentStatus status;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date enrolledAt;

    private UserDto user;
    private CourseDto course;
}
