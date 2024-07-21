package ec.edu.espe.msvc.courses.controller;

import ec.edu.espe.msvc.courses.dto.UserCourseDto;
import ec.edu.espe.msvc.courses.dto.UserDto;
import ec.edu.espe.msvc.courses.entity.UserCourse;
import ec.edu.espe.msvc.courses.service.UserCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-courses")
public class UserCourseController {
    @Autowired
    private UserCourseService userCourseService;

    @GetMapping
    public List<UserCourse> getAll() {
        return userCourseService.getAllUserCourses();
    }

    @PostMapping
    public UserCourseDto registerUserCourse(@RequestParam long userId, @RequestParam long courseId) {
        return userCourseService.registerUserCourse(userId, courseId);
    }

    @DeleteMapping("/unregister")
    public void removeUserCourse(@RequestParam long userId, @RequestParam long courseId) {
        userCourseService.removeUserCourse(userId, courseId);
    }

    @GetMapping("/users/{courseId}")
    public List<UserDto> findUsersByCourseId(@PathVariable Long courseId) {
        return userCourseService.findUsersByCourseId(courseId);
    }
}
