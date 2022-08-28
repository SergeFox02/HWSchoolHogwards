package ru.hogwarts.school;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Student;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.RequestEntity.delete;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SchoolHogwartsApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    StudentController studentController;

    @Autowired
    TestRestTemplate restTemplate;

    private final long ID = 1;
    private final Student STUDENT = new Student();

    @Test
    void contextLoads() throws Exception{
        assertThat(studentController).isNotNull();
    }

    @Test
    public void testDefaultMessage() throws Exception{
        assertThat(this.restTemplate.getForObject("http://localhost:" + port +"/", String.class))
                .isEqualTo("Hello yang Mag, in our School");
    }

    @Test
    public void testGetAllStudents() throws Exception{
        assertThat(this.restTemplate.getForObject("http://localhost:" + port +"/student", String.class))
                .isNotEmpty();
        assertThat(this.restTemplate.getForObject("http://localhost:" + port +"/student", String.class))
                .isNotNull();
    }

    @Test
    public void testGetStudentById() throws Exception{
        assertThat(this.restTemplate.getForObject("http://localhost:" + port +"/student/" + ID, String.class))
                .isNotEmpty();
        assertThat(this.restTemplate.getForObject("http://localhost:" + port +"/student" + ID, String.class))
                .isNotNull();
    }

    @Test
    public void testFilterStudentsByAge() throws Exception{
        final int DEFAULT_AGE = 15;
        assertThat(this.restTemplate.getForObject("http://localhost:" + port +"/student/filter/" + DEFAULT_AGE, String.class))
                .isNotEmpty();
        assertThat(this.restTemplate.getForObject("http://localhost:" + port +"/student/filter/" + DEFAULT_AGE, String.class))
                .isNotNull();
    }

    @Test
    public void testFilterStudentsBetweenMinAndMaxAge() throws Exception{
        final int MIN_AGE = 15;
        final int MAX_AGE = 20;
        assertThat(this.restTemplate.getForObject("http://localhost:" + port +"/student/filter/?"
                        + "minAge=" + MIN_AGE+ "&maxAge=" + MAX_AGE, String.class))
                .isNotEmpty();
        assertThat(this.restTemplate.getForObject("http://localhost:" + port +"/student/filter/?"
                        + "minAge=" + MIN_AGE+ "&maxAge=" + MAX_AGE, String.class))
                .isNotNull();
    }

    @Test
    public void testFindFacultyOfStudentById() throws Exception{
        assertThat(this.restTemplate.getForObject("http://localhost:" + port +"/student/findFaculty/" + ID, String.class))
                .isNotEmpty();
        assertThat(this.restTemplate.getForObject("http://localhost:" + port +"/student/findFaculty/" + ID, String.class))
                .isNotNull();
    }

    @Test
    public void testFindAvatarOfStudentById() throws Exception{
        assertThat(this.restTemplate.getForObject("http://localhost:" + port
                        +"/student/" + ID +"/avatar", String.class))
                .isNotEmpty();
        assertThat(this.restTemplate.getForObject("http://localhost:" + port
                        +"/student" + ID + "/avatar", String.class))
                .isNotNull();
    }

    @Test
    public void testFindAvatarInDataBaseOfStudentById() throws Exception{
        assertThat(this.restTemplate.getForObject("http://localhost:" + port
                        +"/student/" + ID +"/avatar/dataBase", String.class))
                .isNotEmpty();
        assertThat(this.restTemplate.getForObject("http://localhost:" + port
                        +"/student" + ID + "/avatar/dataBase", String.class))
                .isNotNull();
    }

    @Test
    public void testCreateStudent() throws Exception{
        assertThat(this.restTemplate.postForObject("http://localhost:" + port +"/student", STUDENT, String.class))
                .isNotEmpty();
    }

    @Test
    public void testUploadAvatar() throws Exception{
        assertThat(this.restTemplate.postForObject("http://localhost:" + port
                        +"/student/" + ID + "/avatar", STUDENT, String.class))
                .isNotEmpty();
    }

    @Test
    public void testEditStudent() throws Exception{
        restTemplate.put("http://localhost:" + port +"/student", STUDENT);
    }

    @Test
    public void testDeleteStudent() throws Exception{
        delete("http://localhost:" + port +"/student/" + ID);
    }
}
