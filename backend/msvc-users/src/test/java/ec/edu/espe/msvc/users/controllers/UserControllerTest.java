package ec.edu.espe.msvc.users.controllers;

import ec.edu.espe.msvc.users.dto.UserDto;
import ec.edu.espe.msvc.users.entity.User;
import ec.edu.espe.msvc.users.services.UserService;
import ec.edu.espe.msvc.users.utils.TestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Test
    void shouldReturnAllUsers() {
        List<User> users = List.of(
                TestData.getInstance().user(),
                TestData.getInstance().user()
        );

        when(userService.getAll()).thenReturn(users);
        List<User> result = userController.getAll();

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    void shouldReturnUserById() {
        User user = TestData.getInstance().user();

        when(userService.getById(user.getId())).thenReturn(Optional.of(user));
        ResponseEntity<?> result = userController.getById(user.getId());

        assertThat(result).isNotNull();
        assertThat(result.getBody()).isEqualTo(user);
        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test
    void shouldReturnUserByIdNotFound() {
        User user = TestData.getInstance().user();

        when(userService.getById(user.getId())).thenReturn(Optional.empty());
        ResponseEntity<?> result = userController.getById(user.getId());

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode().is4xxClientError()).isTrue();
    }

    @Test
    void shouldDeleteUser() {
        User user = TestData.getInstance().user();

        when(userService.getById(user.getId())).thenReturn(Optional.of(user));
        ResponseEntity<?> result = userController.delete(user.getId());

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test
    void shouldDeleteUserNotFound() {
        User user = TestData.getInstance().user();

        when(userService.getById(user.getId())).thenReturn(Optional.empty());
        ResponseEntity<?> result = userController.delete(user.getId());

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode().is4xxClientError()).isTrue();
    }

    @Test
    void shouldSaveUser() {
        User user = TestData.getInstance().user();
        when(userService.save(any())).thenReturn(user);

        ResponseEntity<?> result = userController.save(TestData.getInstance().userDto());

        assertThat(result).isNotNull();
        assertThat(result.getBody()).isEqualTo(user);
        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test
    void shouldUpdateUser() {
        User user = TestData.getInstance().user();
        when(userService.update(any())).thenReturn(user);

        ResponseEntity<?> result = userController.update(TestData.getInstance().userDto());

        assertThat(result).isNotNull();
        assertThat(result.getBody()).isEqualTo(user);
        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test
    void shouldFetchUsers() {
        List<UserDto> users = List.of(
                TestData.getInstance().userDto(),
                TestData.getInstance().userDto()
        );

        when(userService.fetchUsersByIds(List.of(1L, 2L))).thenReturn(users);
        List<UserDto> result = userController.fetchUsers(List.of(1L, 2L));

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    void shouldSearchUsers() {
        List<UserDto> users = List.of(
                TestData.getInstance().userDto(),
                TestData.getInstance().userDto()
        );

        when(userService.search("query")).thenReturn(users);
        List<UserDto> result = userController.search("query");

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
    }
}
