package ru.hogwarts.school.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentRepository;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

/**
 * Service for working with students from school Hogwarts
 */

@Service
public class StudentServiceImpl implements StudentService {

    Logger logger = LoggerFactory.getLogger(AvatarService.class);

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    /**
     * Saves the student to the database<br>
     * method is used {@link org.springframework.data.jpa.repository.JpaRepository#save(Object)}
     *
     * @param student created student, must not be {@code null}
     * @return created student.
     */
    @Override
    public Student createStudent(Student student) {
        logger.info("Was invoked method for creat student");
        return studentRepository.save(student);
    }

    /**
     * Find student by id<br>
     * method is used {@link org.springframework.data.jpa.repository.JpaRepository#findById(Object)}
     *
     * @param id must not be {@code null}.
     * @return found student, if student not found return {@code null}.
     */
    @Override
    public Student findStudent(long id) {
        logger.info("Was invoked method for find student by id = {}", id);
        if (studentRepository.findById(id).isEmpty()){
            return null;
        }
        return studentRepository.findById(id).get();
    }

    /**
     * Edit the student in the database<br>
     * method is used {@link org.springframework.data.jpa.repository.JpaRepository#save(Object)}
     *
     * @param student edit student, must not be {@code null}
     * @return edit student, if student not found in database return {@code null}
     */
    @Override
    public Student editStudent(Student student) {
        logger.info("Was invoked method for edit student");
        if (studentRepository.findById(student.getId()).isEmpty()){
            return null;
        }
        return studentRepository.save(student);
    }

    /**
     * Delete a student from the database<br>
     * method is used {@link org.springframework.data.jpa.repository.JpaRepository#deleteById(Object)}
     *
     * @param id must not be {@code null}
     * @return deleted student from database, if student not found in database return {@code null}
     */
    @Override
    public Student deleteStudent(long id) {
        if (studentRepository.findById(id).isEmpty()){
            logger.info("Student with id {} is not found", id);
            return null;
        }
        Student deleteStudent = studentRepository.findById(id).get();
        studentRepository.deleteById(id);
        logger.info("Student with id = {} is deleted", id);
        return deleteStudent;
    }

    /**
     * Get all students from database<br>
     * method is used {@link JpaRepository#findAll()} (Object)}
     *
     * @return All students from database
     */
    @Override
    public Collection<Student> getAllStudents() {
        logger.info("Was invoked method for get all students");
        return studentRepository.findAll();
    }

    /**
     * Filter students by age<br>
     *
     * @param age age of students, {@code age > 0}
     * @return filtered students by age
     * @throws IllegalArgumentException if age <= 0
     */
    @Override
    public Collection<Student> filterAgeStudents(int age) {
        logger.info("Was invoked method for filter Students by age = {}", age);
        if (age <= 0) {
            throw new IllegalArgumentException("Student age <= 0");
        }
        return studentRepository.findAll().stream()
                .filter(s -> s.getAge() == age)
                .collect(Collectors.toList());
    }

    /**
     * Filter students by age between min age and max age<br>
     *
     * @param min age of students, {@code min > 0}
     * @param max age of students, {@code max >= min}
     * @return filtered students by age
     * @throws IllegalArgumentException if Student age min <= 0 || max <= 0 || max < min
     */
    @Override
    public Collection<Student> filterAgeStudents(int min, int max) {
        logger.info("Was invoked method for filter Students by age between {} and {} age", min, max);
        if (min <= 0 || max <=0 || max < min){
            throw new IllegalArgumentException("Student age min <= 0 || max <= 0 || max < min");
        }
        return studentRepository.findByAgeBetween(min, max);
    }

    /**
     * Find faculty of student
     *
     * @param id student id.
     * @return faculty of student, or return {@code null} when student not found.
     */
    @Override
    public Faculty findFacultyOfStudent(long id) {
        logger.info("Was invoked method for find faculty of Student by id = {} ", id);
        if (studentRepository.findById(id).isEmpty()){
            return null;
        }
        return studentRepository.findById(id).get().getFaculty();
    }

    /**
     * Get amount of students from database
     *
     * @return amount of students
     */
    @Override
    public long getAmountOfStudents() {
        logger.info("Was invoked method for get amount of students");
        return studentRepository.getAmountOfStudents();
    }

    /**
     * Get average age of students from database
     *
     * @return average age
     */
    @Override
    public double getAverageAge() {
        logger.info("Was invoked method for get average age of students ");
        return studentRepository.getAverageAge();
    }

    /**
     * Get five last students from database
     *
     * @return five last students
     */
    @Override
    public Collection<Student> getFiveLastStudents() {
        logger.info("Was invoked method for get five last students by id");
        return studentRepository.getFiveLastStudents();
    }

    /**
     * Filter students with name start with 'A' and sorted
     *
     * @return students with name start with 'A' and sorted
     */
    @Override
    public Collection<String> filterStudentsByOrderStartNameA() {
        return studentRepository.findAll().stream()
                .filter(s -> s.getName().startsWith("A"))
                .map(s -> s.getName().toUpperCase())
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Get average age of students from database with Stream
     *
     * @return average age
     */
    @Override
    public OptionalDouble getAverageAgeWithStream() {
        return studentRepository.findAll().stream()
                .mapToDouble(s -> (double) s.getAge())
                .average();
    }

}
