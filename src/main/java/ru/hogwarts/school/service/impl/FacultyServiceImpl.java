package ru.hogwarts.school.service.impl;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FacultyServiceImpl implements FacultyService {

    private final Map<Long, Faculty> faculties;
    private long lastId;

    public FacultyServiceImpl() {
        this.faculties = new HashMap<>();
    }

    @Override
    public Faculty createFaculty(Faculty faculty) {
        faculty.setId(++lastId);
        faculties.put(lastId, faculty);
        return faculty;
    }

    @Override
    public Faculty findFaculty(long id) {
        return faculties.get(id);
    }

    @Override
    public Faculty editFaculty(Faculty faculty) {
        if (faculties.containsKey(faculty.getId())) {
            return faculties.put(faculty.getId(), faculty);
        }
        return null;
    }

    @Override
    public Faculty deleteFaculty(long id) {
        if (faculties.containsKey(id)) {
            Faculty deleteFaculty = faculties.get(id);
            faculties.remove(id);
            return deleteFaculty;
        }
        return null;
    }

    @Override
    public Collection<Faculty> getAllFaculties() {
        return faculties.values();
    }

    @Override
    public Collection<Faculty> filterFacultiesByColor(String color) {
        return faculties.values().
                stream()
                .filter(c -> c.getColor().equals(color))
                .collect(Collectors.toList());
    }
}
