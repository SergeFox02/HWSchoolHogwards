package ru.hogwarts.school.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.FacultyRepository;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class FacultyServiceImpl implements FacultyService {

    Logger logger = LoggerFactory.getLogger(AvatarService.class);

    @Autowired
    private final FacultyRepository facultyRepository;

    public FacultyServiceImpl(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    @Override
    public Faculty createFaculty(Faculty faculty) {
        logger.info("Was invoked method for creat faculty");
        return facultyRepository.save(faculty);
    }

    @Override
    public Faculty findFaculty(long id){
        logger.info("Was invoked method for find faculty by id = {}", id);
        if (facultyRepository.findById(id).isEmpty()){
            return null;
        }
        return facultyRepository.findById(id).get();
    }

    @Override
    public Faculty editFaculty(Faculty faculty) {
        logger.info("Was invoked method for edit faculty = {}", faculty);
        if (facultyRepository.findById(faculty.getId()).isEmpty()){
            return null;
        }
        return facultyRepository.save(faculty);
    }

    @Override
    public Faculty deleteFaculty(long id) {
        if (facultyRepository.findById(id).isEmpty()){
            logger.info("Faculty with id {} is not found", id);
            return null;
        }
        Faculty deleteFaculty = facultyRepository.findById(id).get();
        facultyRepository.deleteById(id);
        logger.info("Faculty with id = {} is deleted", id);
        return deleteFaculty;
    }

    @Override
    public Collection<Faculty> getAllFaculties() {
        logger.info("Was invoked method for get all faculties");
        return facultyRepository.findAll();
    }

    @Override
    public Collection<Faculty> filterFacultiesByColor(String color) {
        logger.info("Was invoked method for filter faculty by color = {}", color);
        return facultyRepository.findAll().
                stream()
                .filter(c -> c.getColor().equals(color))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Faculty> filterFacultiesByColorOrName(String color, String name) {
        logger.info("Was invoked method for filter faculty by color or(and) name");
        if (color == null){
            logger.debug("in method filterByColorOrName color is Null");
        }
        if (name == null){
            logger.debug("in method filterByColorOrName name is Null");
        }
        return facultyRepository.findFacultiesByColorOrNameIgnoreCase(color, name);
    }

    @Override
    public Collection<Student> findStudentsOfFaculty(long id) {
        logger.info("Was invoked method for find students of faculty by id = {}", id);
        return facultyRepository.getById(id).getStudents();
    }

    @Override
    public String longestNameOfFaculty() {
        return facultyRepository.findAll().stream()
                .map(Faculty :: getName)
                .sorted((s1, s2) -> s2.length() - s1.length())
                .findFirst()
                .get();
    }
}
