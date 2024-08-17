package ec.edu.espe.msvc.courses.service;

import ec.edu.espe.msvc.courses.dto.CourseDto;
import ec.edu.espe.msvc.courses.entity.Course;
import ec.edu.espe.msvc.courses.repository.CourseRepository;
import ec.edu.espe.msvc.courses.service.impl.CourseServiceImpl;
import ec.edu.espe.msvc.courses.utils.TestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {
    @InjectMocks
    private CourseService courseService = new CourseServiceImpl();

    @Mock
    private CourseRepository courseRepository;

    @Test
    void shouldReturnAllCourses() {
        List<Course> courses = List.of(
                TestData.getInstance().course(),
                TestData.getInstance().course()
        );

        when(courseRepository.findByOrderByIdDesc()).thenReturn(courses);
        assertThat(courseService.getAll()).hasSize(2);
    }

    @Test
    void shouldReturnCourseById() {
        Course course = TestData.getInstance().course();
        course.setId(1L);

        when(courseRepository.findById(1L)).thenReturn(java.util.Optional.of(course));
        assertThat(courseService.getById(1L).get().getId()).isEqualTo(1L);
    }
}
