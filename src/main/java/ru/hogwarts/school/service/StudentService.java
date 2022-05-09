package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.OptionalDouble;

public interface StudentService {

    Student createStudent(Student student);

    Student findStudent(long id);

    Student editStudent(Student student);

    void deleteStudent(long id);

    Collection<Student> getAllStudents();

    Collection<Student> filterAgeStudents(int age);

    Collection<Student> filterAgeStudents(int min, int max);

    Faculty findFacultyOfStudent(long id);

    long getAmountOfStudents();

    double getAverageAge();

    Collection<Student> getFiveLastStudents();

    Collection<String> filterStudentsBuOrderStartNameA();

    OptionalDouble averageAge();

    int findValue();

    void printAllStudentsToConsole();

    void printAllStudentsToConsoleSynchronized();
}
