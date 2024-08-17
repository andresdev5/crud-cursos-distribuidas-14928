package ec.edu.espe.msvc.courses.service.impl;

import ec.edu.espe.msvc.courses.clients.UserServiceClient;
import ec.edu.espe.msvc.courses.dto.CourseDto;
import ec.edu.espe.msvc.courses.dto.CreateEnrollmentsDto;
import ec.edu.espe.msvc.courses.dto.EnrollmentDto;
import ec.edu.espe.msvc.courses.dto.UserDto;
import ec.edu.espe.msvc.courses.entity.Course;
import ec.edu.espe.msvc.courses.entity.Enrollment;
import ec.edu.espe.msvc.courses.entity.EnrollmentStatus;
import ec.edu.espe.msvc.courses.repository.CourseRepository;
import ec.edu.espe.msvc.courses.repository.EnrollmentRepository;
import ec.edu.espe.msvc.courses.service.EnrollmentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {
    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserServiceClient userServiceClient;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<EnrollmentDto> getAllEnrollments() {
        List<EnrollmentDto> enrollments = enrollmentRepository.findByOrderByIdDesc().stream()
                .map(this::mapToEnrollmentDto)
                .toList();

        addUsersToEnrollmentDto(enrollments);
        return enrollments;
    }

    @Override
    public EnrollmentDto registerEnrollment(Long userId, Long courseId) {
        UserDto user = userServiceClient.getUserById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El id del usuario no existe o es incorrecto."));

        courseRepository.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El id del curso no existe o es incorrecto."));

        boolean exists = enrollmentRepository.existsByUserIdAndCourseIdAndStatus(userId, courseId, EnrollmentStatus.ENROLLED);

        if (exists) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario '" + user.getName() + "' ya está registrado en ese curso.");
        }

        Enrollment enrollment = Enrollment.builder()
                .userId(userId)
                .course(Course.builder().id(courseId).build())
                .enrolledAt(new Date())
                .status(EnrollmentStatus.ENROLLED)
                .build();

        Enrollment saved = enrollmentRepository.save(enrollment);
        EnrollmentDto enrollmentDto = mapToEnrollmentDto(saved);

        return addUserToEnrollmentDto(enrollmentDto);
    }

    @Override
    public List<EnrollmentDto> registerEnrollments(CreateEnrollmentsDto createEnrollmentsDto) {
        List<EnrollmentDto> enrollments = createEnrollmentsDto.getUserIds().stream()
                .map(userId -> registerEnrollment(userId, createEnrollmentsDto.getCourseId()))
                .toList();

        addUsersToEnrollmentDto(enrollments);
        return enrollments;
    }

    @Override
    public List<EnrollmentDto> registerEnrollments(Long courseId, List<Long> userIds) {
        List<EnrollmentDto> enrollments = userIds.stream()
                .map(userId -> registerEnrollment(userId, courseId))
                .toList();

        addUsersToEnrollmentDto(enrollments);
        return enrollments;
    }

    @Override
    public void removeEnrollment(Long userId, Long courseId) {
        Enrollment enrollment = enrollmentRepository.findByUserIdAndCourseId(userId, courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario no está registrado en ese curso."));

        enrollmentRepository.delete(enrollment);
    }

    @Override
    public List<UserDto> findUsersByCourseId(Long courseId) {
        List<Long> userIds = enrollmentRepository.findUserIdsByCourseId(courseId);
        return userServiceClient.fetchUsersById(userIds);
    }

    @Override
    public Map<Long, List<EnrollmentDto>> findAllEnrollmentsMappedByCourse() {
        Map<Long, List<Enrollment>> map = enrollmentRepository.findAllMappedByCourse();
        Map<Long, List<EnrollmentDto>> mappedDto = new HashMap<>();
        List<Long> userIds = map.values().stream()
                .flatMap(List::stream)
                .map(Enrollment::getUserId)
                .toList();

        Map<Long, UserDto> users = userServiceClient.fetchUsersById(userIds).stream()
                .collect(Collectors.toMap(UserDto::getId, Function.identity()));

        map.forEach((courseId, enrollments) -> {
            mappedDto.put(courseId, enrollments.stream()
                    .map(this::mapToEnrollmentDto)
                    .toList());

            mappedDto.get(courseId).forEach(enrollmentDto -> {
                enrollmentDto.setUser(users.get(enrollmentDto.getUserId()));
            });
        });

        return mappedDto;
    }

    @Override
    public void cancelEnrollmentById(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "La matrícula no existe o es incorrecta."));

        if (enrollment.getStatus() == EnrollmentStatus.CANCELED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya se ha cancelado la matrícula");
        }

        enrollment.setStatus(EnrollmentStatus.CANCELED);
        enrollmentRepository.save(enrollment);
    }

    @Override
    public List<EnrollmentDto> findEnrollmentsByUserId(Long userId) {
        return enrollmentRepository.findByUserId(userId).stream()
                .map(this::mapToEnrollmentDto)
                .toList();
    }

    @Override
    public void removeEnrollmentByUserId(Long userId) {
        List<Enrollment> enrollments = enrollmentRepository.findByUserId(userId);

        if (enrollments.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario no tiene matrículas registradas.");
        }

        enrollmentRepository.deleteAll(enrollments);
    }

    private EnrollmentDto mapToEnrollmentDto(Enrollment enrollment) {
        return EnrollmentDto.builder()
                .id(enrollment.getId())
                .userId(enrollment.getUserId())
                .courseId(enrollment.getCourse().getId())
                .status(enrollment.getStatus())
                .course(CourseDto.builder()
                        .id(enrollment.getCourse().getId())
                        .name(enrollment.getCourse().getName())
                        .description(enrollment.getCourse().getDescription())
                        .createdAt(enrollment.getCourse().getCreatedAt())
                        .updatedAt(enrollment.getCourse().getUpdatedAt())
                        .enrollments(null)
                        .build())
                .enrolledAt(enrollment.getEnrolledAt())
                .build();
    }

    private EnrollmentDto addUserToEnrollmentDto(EnrollmentDto enrollmentDto) {
        UserDto user = userServiceClient.getUserById(enrollmentDto.getUserId()).orElse(null);
        enrollmentDto.setUser(user);
        return enrollmentDto;
    }

    private void addUsersToEnrollmentDto(List<EnrollmentDto> enrollments) {
        List<Long> userIds = enrollments.stream()
                .map(EnrollmentDto::getUserId)
                .toList();

        Map<Long, UserDto> users = userServiceClient.fetchUsersById(userIds).stream()
                .collect(Collectors.toMap(UserDto::getId, Function.identity()));

        enrollments.forEach(enrollment -> {
            enrollment.setUser(users.get(enrollment.getUserId()));
        });
    }
}
