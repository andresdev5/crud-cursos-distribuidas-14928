package ec.edu.espe.msvc.courses.service.impl;

import ec.edu.espe.msvc.courses.clients.UserServiceClient;
import ec.edu.espe.msvc.courses.dto.UserCourseDto;
import ec.edu.espe.msvc.courses.dto.UserDto;
import ec.edu.espe.msvc.courses.entity.Course;
import ec.edu.espe.msvc.courses.entity.UserCourse;
import ec.edu.espe.msvc.courses.repository.CourseRepository;
import ec.edu.espe.msvc.courses.repository.UserCourseRepository;
import ec.edu.espe.msvc.courses.service.UserCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;

@Service
public class UserCourseServiceImpl implements UserCourseService {
    @Autowired
    private UserCourseRepository userCourseRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserServiceClient userServiceClient;

    @Override
    public UserCourseDto registerUserCourse(Long userId, Long courseId) {
        userServiceClient.getUserById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El id del usuario no existe o es incorrecto."));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El id del curso no existe o es incorrecto."));

        boolean exists = userCourseRepository.existsByUserIdAndCourseId(userId, courseId);

        if (exists) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario ya está registrado en ese curso.");
        }

        UserCourse userCourse = UserCourse.builder()
                .userId(userId)
                .courseId(courseId)
                .enrolledAt(new Date())
                .status("ENROLLED")
                .build();

        UserCourse saved = userCourseRepository.save(userCourse);

        return UserCourseDto.builder()
                .id(saved.getId())
                .userId(saved.getUserId())
                .courseId(saved.getCourseId())
                .enrolledAt(saved.getEnrolledAt())
                .status(saved.getStatus())
                .build();
    }

    @Override
    public void removeUserCourse(Long userId, Long courseId) {
        UserCourse userCourse = userCourseRepository.findByUserIdAndCourseId(userId, courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario no está registrado en ese curso."));

        userCourseRepository.delete(userCourse);
    }

    @Override
    public List<UserCourse> getAllUserCourses() {
        return userCourseRepository.findAll();
    }

    @Override
    public List<UserDto> findUsersByCourseId(Long courseId) {
        List<Long> userIds = userCourseRepository.findUserIdsByCourseId(courseId);
        return userServiceClient.fetchUsersById(userIds);
    }
}
