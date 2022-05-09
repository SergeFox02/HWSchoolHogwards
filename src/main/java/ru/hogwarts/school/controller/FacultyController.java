package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;

@RestController
@RequestMapping("/faculty")
public class FacultyController {

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping
    public ResponseEntity<Collection<Faculty>> getAllFaculties() {
        return ResponseEntity.ok(facultyService.getAllFaculties());
    }

    @GetMapping("{id}")
    public ResponseEntity<Faculty> findFaculty(@PathVariable Long id) {
        Faculty faculty = facultyService.findFaculty(id);
        if (faculty == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(faculty);
    }

    @GetMapping("/filter/{color}")
    public Collection<Faculty> filterFacultiesByColor(@PathVariable String color) {
        return facultyService.filterFacultiesByColor(color);
    }

    @GetMapping("filter")
    public Collection<Faculty> filterFacultyByColorOrName(@RequestParam(required = false) String color,
                                                          @RequestParam(required = false) String name) {
        return facultyService.filterFacultiesByColorOrName(color, name);
    }

    @GetMapping("/findStudentsOfFaculty/{id}")
    public Collection<Student> findStudentsOfFaculty(@PathVariable Long id) {
        return facultyService.findStudentsOfFaculty(id);
    }

    @GetMapping("/longest-name")
    public String longestNameOfFaculty() {
        return facultyService.longestNameOfFaculty();
    }

    @PostMapping
    public Faculty createFaculty(@RequestBody Faculty faculty) {
        return facultyService.createFaculty(faculty);
    }

    @PutMapping
    public ResponseEntity<Faculty> editFaculty(@RequestBody Faculty faculty) {
        Faculty editFaculty = facultyService.editFaculty(faculty);
        if (editFaculty == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(faculty);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Faculty> deleteFaculty(@PathVariable Long id) {
        facultyService.deleteFaculty(id);
        return ResponseEntity.ok().build();
    }
}
