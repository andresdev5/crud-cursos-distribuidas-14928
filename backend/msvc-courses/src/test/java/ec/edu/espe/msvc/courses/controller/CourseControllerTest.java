package ec.edu.espe.msvc.courses.controller;

import ec.edu.espe.msvc.courses.dto.CourseDto;
import ec.edu.espe.msvc.courses.service.CourseService;
import ec.edu.espe.msvc.courses.utils.TestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CourseControllerTest {
    @InjectMocks
    private CourseController courseController;

    @Mock
    private CourseService courseService;

    @Test
    void shouldReturnAllCourses() {
        List<CourseDto> coursesList = List.of(
                TestData.getInstance().courseDto(),
                TestData.getInstance().courseDto()
        );

        when(courseService.getAll()).thenReturn(coursesList);
        assertThat(courseController.getAll()).hasSize(2);
    }

    @Test
    void shouldReturnCourseById() {
        CourseDto courseDto = TestData.getInstance().courseDto();
        courseDto.setId(1L);

        when(courseService.getById(1L)).thenReturn(Optional.of(courseDto));
        assertThat(courseController.getById(1L).getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(courseController.getById(1L).getBody().getId()).isEqualTo(1L);
    }

    @Test
    void shouldDeleteCourseById() {
        CourseDto courseDto = TestData.getInstance().courseDto();
        courseDto.setId(1L);

        when(courseService.getById(1L)).thenReturn(Optional.of(courseDto));
        assertThat(courseController.delete(1L).getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test
    void shouldUpdateCourse() {
        CourseDto courseDto = TestData.getInstance().courseDto();
        courseDto.setId(1L);

        when(courseService.update(courseDto)).thenReturn(courseDto);
        assertThat(courseController.update(courseDto).getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test
    void shouldSaveCourse() {
        CourseDto courseDto = TestData.getInstance().courseDto();
        courseDto.setId(1L);

        when(courseService.save(courseDto)).thenReturn(courseDto);
        assertThat(courseController.save(courseDto).getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test
    void shouldReturnCoursesByQuery() {
        List<CourseDto> coursesList = List.of(
                TestData.getInstance().courseDto(),
                TestData.getInstance().courseDto()
        );

        when(courseService.search("query")).thenReturn(coursesList);
        assertThat(courseController.search("query")).hasSize(2);
    }
}
