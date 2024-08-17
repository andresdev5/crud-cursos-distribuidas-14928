package ec.edu.espe.msvc.courses.controller;

import ec.edu.espe.msvc.courses.dto.EnrollmentDto;
import ec.edu.espe.msvc.courses.dto.UserDto;
import ec.edu.espe.msvc.courses.service.EnrollmentService;
import ec.edu.espe.msvc.courses.utils.TestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EnrollmentControllerTest {
    @InjectMocks
    private EnrollmentController enrollmentController;

    @Mock
    private EnrollmentService enrollmentService;

    @Test
    void shouldGetAll() {
        List<EnrollmentDto> enrollmentDtos = List.of(
                TestData.getInstance().enrollmentDto(),
                TestData.getInstance().enrollmentDto()
        );

        when(enrollmentService.getAllEnrollments()).thenReturn(enrollmentDtos);

        List<EnrollmentDto> result = enrollmentController.getAll();

        verify(enrollmentService, times(1)).getAllEnrollments();
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
    }

    @Test
    void shouldCreateEnrollments() {
        List<EnrollmentDto> enrollmentDtos = List.of(
                TestData.getInstance().enrollmentDto(),
                TestData.getInstance().enrollmentDto()
        );

        when(enrollmentService.registerEnrollments(any())).thenReturn(enrollmentDtos);

        List<EnrollmentDto> result = enrollmentController.createEnrollments(TestData.getInstance().createEnrollmentsDto());

        verify(enrollmentService, times(1)).registerEnrollments(any());
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
    }

    @Test
    void shouldRegisterMultipleUserCourses() {
        List<EnrollmentDto> enrollmentDtos = List.of(
                TestData.getInstance().enrollmentDto(),
                TestData.getInstance().enrollmentDto()
        );

        when(enrollmentService.registerEnrollments(1L, List.of(1L, 2L))).thenReturn(enrollmentDtos);

        List<EnrollmentDto> result = enrollmentController.registerMultipleUserCourses(1L, List.of(1L, 2L));

        verify(enrollmentService, times(1)).registerEnrollments(1L, List.of(1L, 2L));
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
    }

    @Test
    void shouldRemoveUserCourse() {
        enrollmentController.removeUserCourse(1L, 1L);
        verify(enrollmentService, times(1)).removeEnrollment(1L, 1L);
    }

    @Test
    void shouldFindUsersByCourseId() {
        List<UserDto> users = List.of(
                TestData.getInstance().userDto(),
                TestData.getInstance().userDto()
        );

        when(enrollmentService.findUsersByCourseId(1L)).thenReturn(users);

        List<UserDto> result = enrollmentController.findUsersByCourseId(1L);

        verify(enrollmentService, times(1)).findUsersByCourseId(1L);
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
    }

    @Test
    void shouldFindAllEnrollmentsMappedByCourse() {
        Map<Long, List<EnrollmentDto>> enrollments = Map.of(
                1L, List.of(TestData.getInstance().enrollmentDto(), TestData.getInstance().enrollmentDto()),
                2L, List.of(TestData.getInstance().enrollmentDto())
        );

        when(enrollmentService.findAllEnrollmentsMappedByCourse()).thenReturn(enrollments);

        Map<Long, List<EnrollmentDto>> result = enrollmentController.findAllEnrollmentsMappedByCourse();

        verify(enrollmentService, times(1)).findAllEnrollmentsMappedByCourse();
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
    }
}
