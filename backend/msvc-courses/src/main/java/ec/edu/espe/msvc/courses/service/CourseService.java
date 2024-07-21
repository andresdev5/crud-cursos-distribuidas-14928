package ec.edu.espe.msvc.courses.service;

import ec.edu.espe.msvc.courses.dto.CourseDto;
import ec.edu.espe.msvc.courses.entity.Course;

import java.util.List;
import java.util.Optional;

public interface CourseService {
    List<Course> getAll();
    Optional<Course> getById(Long id);
    Course save(CourseDto course);
    void delete(Long id);
    Course update(CourseDto course);
}
