package ru.hogwarts.school.service.impl;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    private final Map<Long, Student> students;
    private long lastId;

    public StudentServiceImpl() {
        this.students = new HashMap<>();
    }

    @Override
    public Student createStudent(Student student) {
        student.setId(++lastId);
        students.put(lastId, student);
        return student;
    }

    @Override
    public Student findStudent(long id) {
        return students.get(id);
    }

    @Override
    public Student editStudent(Student student) {
        if (students.containsKey(student.getId())) {
            return students.put(student.getId(), student);
        }
        return null;
    }

    @Override
    public Student deleteStudent(long id) {
        if (students.containsKey(id)) {
            Student removeStudent = students.get(id);
            students.remove(id);
            return removeStudent;
        }
        return null;
    }

    @Override
    public Collection<Student> getAllStudents() {
        return students.values();
    }

    @Override
    public Collection<Student> filterAgeStudents(int age) {
        return students.values().stream()
                .filter(s -> s.getAge() == age)
                .collect(Collectors.toList());
    }
}
