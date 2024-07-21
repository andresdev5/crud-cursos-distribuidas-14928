package ec.edu.espe.msvc.courses.clients;

import ec.edu.espe.msvc.courses.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@FeignClient(name = "user-service", url = "${feign.url.user-service}")
public interface UserServiceClient {
    @GetMapping("/users/{id}")
    Optional<UserDto> getUserById(@PathVariable long id);

    @PostMapping("/users/fetch-ids")
    List<UserDto> fetchUsersById(@RequestParam List<Long> ids);
}