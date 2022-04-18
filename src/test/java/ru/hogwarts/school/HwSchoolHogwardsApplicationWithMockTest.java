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
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
public class HwSchoolHogwardsApplicationWithMockTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyRepository facultyRepository;

    @SpyBean
    private FacultyService facultyService;

    @InjectMocks
    private FacultyController facultyController;

    private final long ID = 1L;
    private final Faculty FACULTY_1 = new Faculty();
    private final Faculty FACULTY_2 = new Faculty();
    private final Collection<Faculty> FACULTIES = new HashSet<>(List.of(FACULTY_1, FACULTY_2));

    @BeforeEach
    private void StartData() {
        FACULTY_1.setId(1L);
        FACULTY_1.setName("Holly");
        FACULTY_1.setColor("white");
    }

    @Test
    public void FindFacultyById() throws Exception {
        when(facultyService.findFaculty(any(Long.class))).thenReturn(FACULTY_1);
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(FACULTY_1));
        mockMvc.perform(MockMvcRequestBuilders
                .get("http://localhost:8080/faculty/" + ID)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    public void TestGetAllFaculties() throws Exception {
        when(facultyService.getAllFaculties()).thenReturn(FACULTIES);
        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/faculty")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void SaveFaculty() throws Exception {
        final long ID = 1L;
        final String NAME = "Holly";
        final String COLOR = "white";

        JSONObject facultyObject = new JSONObject();
        facultyObject.put("name", NAME);
        facultyObject.put("color", COLOR);

        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName(NAME);
        faculty.setColor(COLOR);

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("http://localhost:8080/faculty/")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.color").value(COLOR));
    }
}
