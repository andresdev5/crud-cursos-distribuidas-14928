package ec.edu.espe.msvc.users.services;


import ec.edu.espe.msvc.users.client.CourseServiceClient;
import ec.edu.espe.msvc.users.dto.EnrollmentDto;
import ec.edu.espe.msvc.users.dto.UserDto;
import ec.edu.espe.msvc.users.entity.User;
import ec.edu.espe.msvc.users.repositories.UserRepository;
import ec.edu.espe.msvc.users.utils.TestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService = new UserServiceImpl();

    @Mock
    private UserRepository userRepository;

    @Mock
    private CourseServiceClient courseServiceClient;

    @Test
    void shouldReturnAllUsers() {
        List<User> users = List.of(
                TestData.getInstance().user(),
                TestData.getInstance().user()
        );

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAll();

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    void shouldReturnUserById() {
        User user = TestData.getInstance().user();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        Optional<User> result = userService.getById(user.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(user.getId());
    }

    @Test
    void shouldSaveUser() {
        User user = TestData.getInstance().user();
        UserDto userDto = UserDto.builder()
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();

        when(userRepository.save(any())).thenReturn(user);

        User result = userService.save(userDto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(user.getId());
        assertThat(result.getName()).isEqualTo(userDto.getName());
        assertThat(result.getEmail()).isEqualTo(userDto.getEmail());
        assertThat(result.getPassword()).isEqualTo(userDto.getPassword());
    }

    @Test
    void shouldUpdateUser() {
        User user = TestData.getInstance().user();
        UserDto userDto = UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);

        User result = userService.update(userDto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(user.getId());
        assertThat(result.getName()).isEqualTo(userDto.getName());
        assertThat(result.getEmail()).isEqualTo(userDto.getEmail());
        assertThat(result.getPassword()).isEqualTo(userDto.getPassword());
    }

    @Test
    void shouldDeleteUser() {
        User user = TestData.getInstance().user();
        List<EnrollmentDto> enrollments = List.of(
                TestData.getInstance().enrollmentDto(),
                TestData.getInstance().enrollmentDto()
        );

        when(userRepository.existsById(user.getId())).thenReturn(true);
        when(courseServiceClient.getEnrollmentsByUserId(any())).thenReturn(List.of());

        userService.delete(user.getId());

        verify(userRepository, times(1)).deleteById(user.getId());
    }

    @Test
    void shouldThrowExceptionOnDeleteByIdWhenUserHasEnrollments() {
        User user = TestData.getInstance().user();
        List<EnrollmentDto> enrollments = List.of(
                TestData.getInstance().enrollmentDto(),
                TestData.getInstance().enrollmentDto()
        );

        when(userRepository.existsById(user.getId())).thenReturn(true);
        when(courseServiceClient.getEnrollmentsByUserId(any())).thenReturn(enrollments);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userService.delete(user.getId());
        });

        assertThat(exception).isInstanceOf(RuntimeException.class);
        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(exception.getMessage()).contains("El usuario tiene cursos registrados, por favor elimine las matrículas asociadas antes de eliminar el usuario.");
    }

    @Test
    void shouldThrowExceptionOnDeleteByIdWhenUserNotFound() {
        User user = TestData.getInstance().user();

        when(userRepository.existsById(user.getId())).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userService.delete(user.getId());
        });

        assertThat(exception).isInstanceOf(RuntimeException.class);
        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(exception.getMessage()).contains("El id del usuario no existe o es incorrecto.");
    }
}
