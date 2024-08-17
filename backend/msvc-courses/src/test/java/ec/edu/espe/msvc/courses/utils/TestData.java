package ec.edu.espe.msvc.courses.utils;

import com.github.javafaker.Faker;
import ec.edu.espe.msvc.courses.dto.CourseDto;
import ec.edu.espe.msvc.courses.dto.CreateEnrollmentsDto;
import ec.edu.espe.msvc.courses.dto.EnrollmentDto;
import ec.edu.espe.msvc.courses.dto.UserDto;
import ec.edu.espe.msvc.courses.entity.Course;

import java.util.ArrayList;
import java.util.List;

public class TestData {
    private static final TestData INSTANCE = new TestData();
    private Faker faker = new Faker();

    public static TestData getInstance() {
        return INSTANCE;
    }

    public Course course() {
        return Course.builder()
                .id(faker.number().randomNumber())
                .name(faker.educator().course())
                .description(faker.lorem().sentence())
                .createdAt(faker.date().birthday())
                .updatedAt(null)
                .build();
    }

    public CourseDto courseDto() {
        return CourseDto.builder()
                .id(faker.number().randomNumber())
                .name(faker.educator().course())
                .description(faker.lorem().sentence())
                .enrollments(new ArrayList<>())
                .createdAt(faker.date().birthday())
                .updatedAt(null)
                .build();
    }

    public EnrollmentDto enrollmentDto() {
        CourseDto courseDto = courseDto();
        UserDto userDto = userDto();

        return EnrollmentDto.builder()
                .id(faker.number().randomNumber())
                .courseId(courseDto.getId())
                .userId(userDto.getId())
                .course(courseDto)
                .user(userDto)
                .enrolledAt(faker.date().birthday())
                .build();
    }

    public UserDto userDto() {
        return UserDto.builder()
                .id(faker.number().randomNumber())
                .name(faker.name().fullName())
                .email(faker.internet().emailAddress())
                .password(faker.internet().password())
                .build();
    }

    public CreateEnrollmentsDto createEnrollmentsDto() {
        List<Long> ids = List.of(
                faker.number().randomNumber(),
                faker.number().randomNumber()
        );

        return CreateEnrollmentsDto.builder()
                .courseId(faker.number().randomNumber())
                .userIds(ids)
                .build();
    }
}
