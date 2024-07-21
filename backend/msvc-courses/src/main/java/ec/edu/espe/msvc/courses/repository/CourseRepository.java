package ec.edu.espe.msvc.courses.repository;

import ec.edu.espe.msvc.courses.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {}
