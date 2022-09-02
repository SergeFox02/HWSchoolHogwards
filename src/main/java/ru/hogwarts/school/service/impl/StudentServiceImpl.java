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
     */
    @Override
    public Collection<Student> filterAgeStudents(int age) {
        logger.info("Was invoked method for filter Students by age = {}", age);
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
     */
    @Override
    public Collection<Student> filterAgeStudents(int min, int max) {
        logger.info("Was invoked method for filter Students by age between {} and {} age", min, max);
        return studentRepository.findByAgeBetween(min, max);
    }

    @Override
    public Faculty findFacultyOfStudent(long id) {
        logger.info("Was invoked method for find faculty of Student by id = {} ", id);
        if (studentRepository.findById(id).isEmpty()){
            return null;
        }
        return studentRepository.findById(id).get().getFaculty();
    }

    @Override
    public long getAmountOfStudents() {
        logger.info("Was invoked method for get amount of students");
        return studentRepository.getAmountOfStudents();
    }

    @Override
    public double getAverageAge() {
        logger.info("Was invoked method for get average age of students ");
        return studentRepository.getAverageAge();
    }

    @Override
    public Collection<Student> getFiveLastStudents() {
        logger.info("Was invoked method for get five last students by id");
        return studentRepository.getFiveLastStudents();
    }

    @Override
    public Collection<String> filterStudentsBuOrderStartNameA() {
        return studentRepository.findAll().stream()
                .filter(s -> s.getName().startsWith("A"))
                .map(s -> s.getName().toUpperCase())
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public OptionalDouble averageAge() {
        return studentRepository.findAll().stream()
                .mapToDouble(s -> (double) s.getAge())
                .average();
    }

}
