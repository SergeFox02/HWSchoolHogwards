package ru.hogwarts.school.controller;

import net.minidev.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.FacultyRepository;
import ru.hogwarts.school.service.impl.FacultyServiceImpl;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FacultyController.class)
class FacultyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    FacultyRepository facultyRepository;

    @SpyBean
    private FacultyServiceImpl facultyService;

    @InjectMocks
    private FacultyController facultyController;

    private final JSONObject facultyObject = new JSONObject();

    private final static String LOCAL_URL = "http://localhost:8080/faculty/";
    private final static Long ID = 1l;
    private final static String NAME_OF_FACULTY = "Black magic";
    private final static String COLOR_OF_FACULTY = "black";
    private final static Faculty FACULTY = new Faculty();
    private final static Collection<Student> STUDENTS = new ArrayList<>();
    private final static Collection<Faculty> FACULTIES = new LinkedList<>();

    @BeforeEach
    private void StartData() {
        facultyObject.put("id", ID);
        facultyObject.put("name", NAME_OF_FACULTY);
        facultyObject.put("color", COLOR_OF_FACULTY);
        facultyObject.put("students", STUDENTS);
        FACULTY.setId(ID);
        FACULTY.setName(NAME_OF_FACULTY);
        FACULTY.setColor(COLOR_OF_FACULTY);
        FACULTY.setStudents(STUDENTS);
    }

    @Test
    public void contextLoads(){
        assertThat(facultyController).isNotNull();
    }

    @Test
    public void FindFaculty() throws Exception{
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(FACULTY));
        mockMvc.perform(MockMvcRequestBuilders
                        .get(LOCAL_URL + ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.name").value(NAME_OF_FACULTY))
                .andExpect(jsonPath("$.color").value(COLOR_OF_FACULTY));
    }

    @Test
    public void GetAllFaculties() throws Exception{
        when(facultyRepository.findAll()).thenReturn((List<Faculty>) FACULTIES);
        mockMvc.perform(MockMvcRequestBuilders
                        .get(LOCAL_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(FACULTIES));
    }

    @Test
    public void CreateFaculty() throws Exception {
        when(facultyRepository.save(any(Faculty.class))).thenReturn(FACULTY);
        mockMvc.perform(MockMvcRequestBuilders
                        .post(LOCAL_URL)
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.name").value(NAME_OF_FACULTY))
                .andExpect(jsonPath("$.color").value(COLOR_OF_FACULTY));
    }

    @Test
    public void EditFaculty() throws Exception {
        when(facultyRepository.save(any(Faculty.class))).thenReturn(FACULTY);
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(FACULTY));
        mockMvc.perform(MockMvcRequestBuilders
                        .put(LOCAL_URL)
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.name").value(NAME_OF_FACULTY))
                .andExpect(jsonPath("$.color").value(COLOR_OF_FACULTY));
    }

    @Test
    public void EditFacultyIfNotFound() throws Exception {
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders
                        .put(LOCAL_URL)
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void DeleteFaculty() throws Exception {
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(FACULTY));
        mockMvc.perform(MockMvcRequestBuilders
                        .delete(LOCAL_URL + ID)
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.name").value(NAME_OF_FACULTY))
                .andExpect(jsonPath("$.color").value(COLOR_OF_FACULTY));
    }

    @Test
    public void DeleteFacultyIfNotFound() throws Exception {
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders
                        .delete(LOCAL_URL + ID)
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


}