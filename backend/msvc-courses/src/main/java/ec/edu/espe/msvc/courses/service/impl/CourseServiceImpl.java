package ec.edu.espe.msvc.courses.service.impl;

import ec.edu.espe.msvc.courses.dto.CourseDto;
import ec.edu.espe.msvc.courses.dto.EnrollmentDto;
import ec.edu.espe.msvc.courses.entity.Course;
import ec.edu.espe.msvc.courses.repository.CourseRepository;
import ec.edu.espe.msvc.courses.service.CourseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<CourseDto> getAll() {
        return courseRepository.findByOrderByIdDesc().stream()
                .map(this::mapToCourseDto)
                .toList();
    }

    @Override
    public Optional<CourseDto> getById(Long id) {
        return courseRepository.findById(id).map(this::mapToCourseDto);
    }

    @Override
    public CourseDto save(CourseDto course) {
        Course entity = modelMapper.map(course, Course.class);
        entity.setId(null);
        entity.setCreatedAt(new Date());
        return mapToCourseDto(courseRepository.save(entity));
    }

    @Override
    public void delete(Long id) {
        courseRepository.deleteById(id);
    }

    @Override
    public CourseDto update(CourseDto course) {
        Course entity = courseRepository.findById(course.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El id del curso no existe o es incorrecto."));

        if (course.getName() != null && !course.getName().isEmpty()) {
            entity.setName(course.getName());
        }

        entity.setDescription(course.getDescription());
        entity.setUpdatedAt(new Date());

        return mapToCourseDto(courseRepository.save(entity));
    }

    @Override
    public List<CourseDto> search(String query) {
        return courseRepository.findByNameContainingIgnoreCase(query).stream()
                .map(this::mapToCourseDto)
                .toList();
    }

    private CourseDto mapToCourseDto(Course course) {
        return CourseDto.builder()
                .id(course.getId())
                .name(course.getName())
                .description(course.getDescription())
                .createdAt(course.getCreatedAt())
                .updatedAt(course.getUpdatedAt())
                .enrollments(course.getEnrollments().stream()
                        .map(enrollment -> EnrollmentDto.builder()
                                .id(enrollment.getId())
                                .courseId(enrollment.getCourse().getId())
                                .userId(enrollment.getUserId())
                                .status(enrollment.getStatus())
                                .enrolledAt(enrollment.getEnrolledAt())
                                .course(null)
                                .build())
                        .toList())
                .build();
    }
}
