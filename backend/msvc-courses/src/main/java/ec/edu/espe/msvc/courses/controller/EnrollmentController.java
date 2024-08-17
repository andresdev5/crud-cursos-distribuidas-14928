package ec.edu.espe.msvc.courses.controller;

import ec.edu.espe.msvc.courses.dto.CreateEnrollmentsDto;
import ec.edu.espe.msvc.courses.dto.EnrollmentDto;
import ec.edu.espe.msvc.courses.dto.UserDto;
import ec.edu.espe.msvc.courses.service.EnrollmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/enrollments")
public class EnrollmentController {
    @Autowired
    private EnrollmentService enrollmentService;

    @GetMapping
    public List<EnrollmentDto> getAll() {
        return enrollmentService.getAllEnrollments();
    }

    @PostMapping
    public List<EnrollmentDto> createEnrollments(@RequestBody @Valid CreateEnrollmentsDto createEnrollmentsDto) {
        return enrollmentService.registerEnrollments(createEnrollmentsDto);
    }

    @PostMapping("/register-multiple")
    public List<EnrollmentDto> registerMultipleUserCourses(@RequestParam long courseId, @RequestBody List<Long> userIds) {
        return enrollmentService.registerEnrollments(courseId, userIds);
    }

    @DeleteMapping
    public void removeUserCourse(@RequestParam long userId, @RequestParam long courseId) {
        enrollmentService.removeEnrollment(userId, courseId);
    }

    @GetMapping("/users/{courseId}")
    public List<UserDto> findUsersByCourseId(@PathVariable Long courseId) {
        return enrollmentService.findUsersByCourseId(courseId);
    }

    @GetMapping("/course-mapped")
    public Map<Long, List<EnrollmentDto>> findAllEnrollmentsMappedByCourse() {
        return enrollmentService.findAllEnrollmentsMappedByCourse();
    }

    @GetMapping(value = "/cancel/{enrollmentId}")
    public void cancelEnrollmentById(@PathVariable Long enrollmentId) {
        enrollmentService.cancelEnrollmentById(enrollmentId);
    }

    @GetMapping("/user/{userId}")
    public List<EnrollmentDto> findEnrollmentsByUserId(@PathVariable Long userId) {
        return enrollmentService.findEnrollmentsByUserId(userId);
    }

    @DeleteMapping("/delete-user-enrollments/{userId}")
    public void deleteEnrollmentByUserId(@PathVariable Long userId) {
        enrollmentService.removeEnrollmentByUserId(userId);
    }
}
