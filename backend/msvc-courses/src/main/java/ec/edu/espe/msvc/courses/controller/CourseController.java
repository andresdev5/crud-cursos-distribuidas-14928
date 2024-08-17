package ec.edu.espe.msvc.courses.controller;

import ec.edu.espe.msvc.courses.dto.CourseDto;
import ec.edu.espe.msvc.courses.entity.Course;
import ec.edu.espe.msvc.courses.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/courses")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @GetMapping
    public List<CourseDto> getAll() {
        return courseService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> getById(@PathVariable Long id){
        return courseService.getById(id).map(course -> ResponseEntity.ok().body(course))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CourseDto> delete(@PathVariable Long id){
        Optional<CourseDto> optionalCourse = courseService.getById(id);

        if(optionalCourse.isPresent()){
            courseService.delete(id);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<CourseDto> save(@Validated(CourseDto.Create.class) @RequestBody CourseDto course) {
        return ResponseEntity.ok().body(courseService.save(course));
    }

    @PutMapping
    public ResponseEntity<CourseDto> update(@Validated(CourseDto.Update.class) @RequestBody CourseDto course) {
        return ResponseEntity.ok().body(courseService.update(course));
    }

    @GetMapping("/search")
    public List<CourseDto> search(@RequestParam String query) {
        return courseService.search(query);
    }
}
