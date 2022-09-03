package ru.hogwarts.school.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.FacultyRepository;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Service for working Faculty from school Hogwarts
 */
@Service
public class FacultyServiceImpl implements FacultyService {

    Logger logger = LoggerFactory.getLogger(AvatarService.class);

    private final FacultyRepository facultyRepository;

    public FacultyServiceImpl(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    /**
     * Add new faculty to the database
     *
     * @param faculty created faculty, must not be {@code null}
     * @return created faculty
     */
    @Override
    public Faculty createFaculty(Faculty faculty) {
        logger.info("Was invoked method for creat faculty");
        return facultyRepository.save(faculty);
    }

    /**
     * Find faculty by {@code id} in database
     *
     * @param id of faculty, must not be {@code null}
     * @return finding faculty, return {@code null} if faculty not found
     */
    @Override
    public Faculty findFaculty(long id){
        logger.info("Was invoked method for find faculty by id = {}", id);
        if (facultyRepository.findById(id).isEmpty()){
            return null;
        }
        return facultyRepository.findById(id).get();
    }

    /**
     * Edit faculty in database
     *
     * @param faculty must not be {@code null}
     * @return edit faculty, return {@code null} if edit faculty not found
     */
    @Override
    public Faculty editFaculty(Faculty faculty) {
        logger.info("Was invoked method for edit faculty = {}", faculty);
        if (facultyRepository.findById(faculty.getId()).isEmpty()){
            return null;
        }
        return facultyRepository.save(faculty);
    }

    /**
     * Delete faculty by {@code id} from database
     *
     * @param id of faculty, must not be {@code null}
     * @return deleting faculty, return {@code null} if faculty not found
     */
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

    /**
     * Get all faculties from database
     *
     * @return faculties from database
     */
    @Override
    public Collection<Faculty> getAllFaculties() {
        logger.info("Was invoked method for get all faculties");
        return facultyRepository.findAll();
    }

    /**
     * Filter faculties by {@code color} from database
     *
     * @param color of faculty, must not be {@code null}
     * @return filter faculties
     * @throws IllegalArgumentException if color is {@code null}
     */
    @Override
    public Collection<Faculty> filterFacultiesByColor(String color) {
        logger.info("Was invoked method for filter faculty by color = {}", color);
        if (color == null) {
            throw new IllegalArgumentException("Color is null");
        }
        return facultyRepository.findAll().
                stream()
                .filter(c -> c.getColor().equals(color))
                .collect(Collectors.toList());
    }

    /**
     * Filter faculties by color/or name
     *
     * @param color of faculty
     * @param name of faculty
     * @return filtered faculties
     */
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

    /**
     * Find students of faculty id
     *
     * @param id faculty, must not be {@code null}
     * @return studens of faculty
     */
    @Override
    public Collection<Student> findStudentsOfFaculty(long id) {
        logger.info("Was invoked method for find students of faculty by id = {}", id);
        return facultyRepository.getById(id).getStudents();
    }

    /**
     * Find the longest name of faculties
     *
     * @return longest name
     */
    @Override
    public String longestNameOfFaculty() {
        logger.info("Was invoked method longestNameOfFaculty");
        return facultyRepository.findAll().stream()
                .map(Faculty :: getName)
                .sorted((s1, s2) -> s2.length() - s1.length())
                .findFirst()
                .get();
    }
}
