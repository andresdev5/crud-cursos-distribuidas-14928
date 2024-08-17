package ec.edu.espe.msvc.courses.repository;

import ec.edu.espe.msvc.courses.entity.Course;
import ec.edu.espe.msvc.courses.entity.Enrollment;
import ec.edu.espe.msvc.courses.entity.EnrollmentStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByOrderByIdDesc();
    boolean existsByUserIdAndCourseId(long userId, long courseId);
    boolean existsByUserIdAndCourseIdAndStatus(long userId, long courseId, EnrollmentStatus status);
    Optional<Enrollment> findByUserIdAndCourseId(long userId, long courseId);
    List<Enrollment> findByUserId(long userId);

    @Query("SELECT userId FROM Enrollment WHERE course.id = :courseId")
    List<Long> findUserIdsByCourseId(long courseId);

    @Query("SELECT e FROM Enrollment e")
    Stream<Enrollment> findAllStream();

    @Transactional
    default Map<Long, List<Enrollment>> findAllMappedByCourse() {
        return findAllStream()
                .collect(Collectors.groupingBy(e -> e.getCourse().getId()));
    }
}
