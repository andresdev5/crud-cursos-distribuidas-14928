package ec.edu.espe.msvc.courses.repository;

import ec.edu.espe.msvc.courses.entity.UserCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserCourseRepository extends JpaRepository<UserCourse, Long> {
    boolean existsByUserIdAndCourseId(Long userId, Long courseId);
    Optional<UserCourse> findByUserIdAndCourseId(Long userId, Long courseId);

    @Query("SELECT userId FROM UserCourse WHERE courseId = :courseId")
    List<Long> findUserIdsByCourseId(Long courseId);
}
