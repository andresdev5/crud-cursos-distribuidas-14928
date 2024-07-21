package ec.edu.espe.msvc.courses.service;

import ec.edu.espe.msvc.courses.dto.UserCourseDto;
import ec.edu.espe.msvc.courses.dto.UserDto;
import ec.edu.espe.msvc.courses.entity.Course;
import ec.edu.espe.msvc.courses.entity.UserCourse;

import java.util.List;

public interface UserCourseService {
    UserCourseDto registerUserCourse(Long userId, Long courseId);
    void removeUserCourse(Long userId, Long courseId);
    List<UserCourse> getAllUserCourses();
    List<UserDto> findUsersByCourseId(Long courseId);
}