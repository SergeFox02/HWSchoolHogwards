package ru.hogwarts.school;

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
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repositories.FacultyRepository;
import ru.hogwarts.school.service.impl.FacultyServiceImpl;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FacultyController.class)
public class SchoolHogwartsApplicationWithMockTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyRepository facultyRepository;

    @SpyBean
    private FacultyServiceImpl facultyService;

    @InjectMocks
    private FacultyController facultyController;

    private final Faculty FACULTY = new Faculty();
    private final long ID = 1L;
    private final String NAME = "Holly";
    private final String COLOR = "white";
    private final JSONObject facultyObject = new JSONObject();

    @BeforeEach
    private void StartData() {
        facultyObject.put("name", NAME);
        facultyObject.put("color", COLOR);
        FACULTY.setId(ID);
        FACULTY.setName(NAME);
        FACULTY.setColor(COLOR);
    }

    @Test
    public void FindFacultyById() throws Exception {
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(FACULTY));
        mockMvc.perform(MockMvcRequestBuilders
                .get("http://localhost:8080/faculty/" + ID)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.color").value(COLOR));
    }

    @Test
    public void TestGetAllFaculties() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/faculty")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    public void TestFacultiesByColor() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/faculty/filter/" + COLOR)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void TestFacultiesByColorOrName() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/faculty/filter/?name=" + NAME + "?color=" + COLOR)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void SaveFaculty() throws Exception {
        when(facultyRepository.save(any(Faculty.class))).thenReturn(FACULTY);
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(FACULTY));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("http://localhost:8080/faculty/")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.color").value(COLOR));
    }

    @Test
    public void EditFaculty() throws Exception {
        when(facultyRepository.save(any(Faculty.class))).thenReturn(FACULTY);
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(FACULTY));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("http://localhost:8080/faculty/")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void DeleteFaculty() throws Exception {
        when(facultyRepository.save(any(Faculty.class))).thenReturn(FACULTY);
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(FACULTY));

        mockMvc.perform(MockMvcRequestBuilders
                .delete("http://localhost:8080/faculty/" + ID)
                .content(facultyObject.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
