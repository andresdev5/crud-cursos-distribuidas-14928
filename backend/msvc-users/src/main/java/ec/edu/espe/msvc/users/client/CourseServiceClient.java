package ec.edu.espe.msvc.users.client;

import ec.edu.espe.msvc.users.dto.EnrollmentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "msvc-courses", url = "${feign.url.courses-service}")
public interface CourseServiceClient {
    @GetMapping("/enrollments/user/{userId}")
    List<EnrollmentDto> getEnrollmentsByUserId(@PathVariable Long userId);
}
