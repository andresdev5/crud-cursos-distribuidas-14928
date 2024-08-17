package ec.edu.espe.msvc.courses.service;

import ec.edu.espe.msvc.courses.dto.CourseDto;
import ec.edu.espe.msvc.courses.entity.Course;

import java.util.List;
import java.util.Optional;

public interface CourseService {
    List<CourseDto> getAll();
    Optional<CourseDto> getById(Long id);
    CourseDto save(CourseDto course);
    void delete(Long id);
    CourseDto update(CourseDto course);
    List<CourseDto> search(String query);
}
