package ec.edu.espe.msvc.courses.service;

import ec.edu.espe.msvc.courses.dto.CreateEnrollmentsDto;
import ec.edu.espe.msvc.courses.dto.EnrollmentDto;
import ec.edu.espe.msvc.courses.dto.UserDto;
import ec.edu.espe.msvc.courses.entity.Enrollment;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface EnrollmentService {
    EnrollmentDto registerEnrollment(Long userId, Long courseId);
    List<EnrollmentDto> registerEnrollments(CreateEnrollmentsDto createEnrollmentsDto);
    List<EnrollmentDto> registerEnrollments(Long courseId, List<Long> userIds);
    void removeEnrollment(Long userId, Long courseId);
    List<EnrollmentDto> getAllEnrollments();
    List<UserDto> findUsersByCourseId(Long courseId);
    Map<Long, List<EnrollmentDto>> findAllEnrollmentsMappedByCourse();
    void cancelEnrollmentById(Long enrollmentId);
    List<EnrollmentDto> findEnrollmentsByUserId(Long userId);
    void removeEnrollmentByUserId(Long userId);
}