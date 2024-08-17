package ec.edu.espe.msvc.users.utils;

import com.github.javafaker.Faker;
import ec.edu.espe.msvc.users.dto.CourseDto;
import ec.edu.espe.msvc.users.dto.EnrollmentDto;
import ec.edu.espe.msvc.users.dto.UserDto;
import ec.edu.espe.msvc.users.entity.User;

import java.util.ArrayList;

public class TestData {
    private static final TestData INSTANCE = new TestData();
    private Faker faker = new Faker();

    public static TestData getInstance() {
        return INSTANCE;
    }

    public User user() {
        return User.builder()
                .id(faker.number().randomNumber())
                .name(faker.name().fullName())
                .email(faker.internet().emailAddress())
                .password(faker.internet().password())
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

    public EnrollmentDto enrollmentDto() {
        return EnrollmentDto.builder()
                .id(faker.number().randomNumber())
                .userId(faker.number().randomNumber())
                .courseId(faker.number().randomNumber())
                .enrolledAt(faker.date().birthday())
                .course(TestData.getInstance().courseDto())
                .user(TestData.getInstance().userDto())
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
                .enrollments(new ArrayList<>())
                .build();
    }
}
