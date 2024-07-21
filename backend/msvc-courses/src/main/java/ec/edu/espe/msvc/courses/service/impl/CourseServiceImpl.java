package ec.edu.espe.msvc.courses.service.impl;

import ec.edu.espe.msvc.courses.dto.CourseDto;
import ec.edu.espe.msvc.courses.entity.Course;
import ec.edu.espe.msvc.courses.repository.CourseRepository;
import ec.edu.espe.msvc.courses.service.CourseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<Course> getAll() {
        return courseRepository.findAll();
    }

    @Override
    public Optional<Course> getById(Long id) {
        return courseRepository.findById(id);
    }

    @Override
    public Course save(CourseDto course) {
        Course entity = modelMapper.map(course, Course.class);
        entity.setId(null);
        return courseRepository.save(entity);
    }

    @Override
    public void delete(Long id) {
        courseRepository.deleteById(id);
    }

    @Override
    public Course update(CourseDto course) {
        Course entity = courseRepository.findById(course.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El id del curso no existe o es incorrecto."));

        if (course.getName() != null && !course.getName().isEmpty()) {
            entity.setName(course.getName());
        }

        return courseRepository.save(entity);
    }
}
