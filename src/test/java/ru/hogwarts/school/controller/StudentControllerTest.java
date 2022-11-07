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
import ru.hogwarts.school.repositories.AvatarRepository;
import ru.hogwarts.school.repositories.StudentRepository;
import ru.hogwarts.school.service.impl.AvatarServiceImpl;
import ru.hogwarts.school.service.impl.StudentServiceImpl;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    StudentRepository studentRepository;

    @MockBean
    AvatarRepository avatarRepository;

    @SpyBean
    private StudentServiceImpl facultyService;


    @SpyBean
    private AvatarServiceImpl avatarService;

    @InjectMocks
    private StudentController studentController;

    private final JSONObject studentObject = new JSONObject();

    private final static String LOCAL_URL = "http://localhost:8080/student/";
    private final static Long ID = 1L;
    private final static String NAME = "Garry";
    private final static int AGE = 18;
    private final static Student STUDENT = new Student();
    private final static Faculty FACULTY = new Faculty();
    private final static Collection<Student> STUDENTS = new ArrayList<>();
    private final static Collection<Faculty> FACULTIES = new LinkedList<>();

    @BeforeEach
    private void StartData() {
        studentObject.put("id", ID);
        studentObject.put("name", NAME);
        studentObject.put("age", AGE);
        studentObject.put("faculty", FACULTY);
        STUDENT.setId(ID);
        STUDENT.setName(NAME);
        STUDENT.setAge(AGE);
        STUDENT.setFaculty(FACULTY);
    }

    @Test
    public void contextLoads(){
        assertThat(studentController).isNotNull();
    }

    @Test
    public void findStudent() throws Exception{
        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(STUDENT));
        mockMvc.perform(MockMvcRequestBuilders
                        .get(LOCAL_URL + ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.age").value(AGE))
                .andExpect(jsonPath("$.faculty").value(FACULTY));
    }

    @Test
    public void getAllStudents() throws Exception{
        when(studentRepository.findAll()).thenReturn((List<Student>) STUDENTS);
        mockMvc.perform(MockMvcRequestBuilders
                        .get(LOCAL_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(STUDENTS));
    }

    @Test
    public void createStudent() throws Exception {
        when(studentRepository.save(any(Student.class))).thenReturn(STUDENT);
        mockMvc.perform(MockMvcRequestBuilders
                        .post(LOCAL_URL)
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.age").value(AGE))
                .andExpect(jsonPath("$.faculty").value(FACULTY));
    }

    @Test
    public void editStudent() throws Exception {
        when(studentRepository.save(any(Student.class))).thenReturn(STUDENT);
        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(STUDENT));
        mockMvc.perform(MockMvcRequestBuilders
                        .put(LOCAL_URL)
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.age").value(AGE))
                .andExpect(jsonPath("$.faculty").value(FACULTY));
    }

    @Test
    public void editStudentIfNotFound() throws Exception {
        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders
                        .put(LOCAL_URL)
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteStudent() throws Exception {
        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(STUDENT));
        mockMvc.perform(MockMvcRequestBuilders
                        .delete(LOCAL_URL + ID)
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.age").value(AGE))
                .andExpect(jsonPath("$.faculty").value(FACULTY));
    }

    @Test
    public void deleteStudentIfNotFound() throws Exception {
        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders
                        .delete(LOCAL_URL + ID)
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}