package ec.edu.espe.msvc.courses.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateEnrollmentsDto {
    @NotNull
    private Long courseId;

    @NotEmpty
    private List<Long> userIds;
}
