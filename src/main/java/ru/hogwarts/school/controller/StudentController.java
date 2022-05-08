package ru.hogwarts.school.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.StudentService;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;
    private final AvatarService avatarService;

    Logger logger = LoggerFactory.getLogger(AvatarService.class);

    public StudentController(StudentService studentService, AvatarService avatarService) {
        this.studentService = studentService;
        this.avatarService = avatarService;
    }

    @GetMapping
    public ResponseEntity<Collection<Student>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("{id}")
    public ResponseEntity<Student> findStudent(@PathVariable Long id) {
        Student student = studentService.findStudent(id);
        if (student == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(student);
    }

    @GetMapping("/filter/{age}")
    public Collection<Student> filterStudentsByAge(@PathVariable Integer age) {
        return studentService.filterAgeStudents(age);
    }

    @GetMapping("filter")
    public Collection<Student> filterStudentsByAgeBetween(@RequestParam Integer minAge,
                                                          @RequestParam Integer maxAge) {
        return studentService.filterAgeStudents(minAge, maxAge);
    }

    @GetMapping("findFaculty/{id}")
    public Faculty findFacultyOfStudent(@PathVariable Long id) {
        return studentService.findFacultyOfStudent(id);
    }

    @GetMapping(value = "/{id}/avatar/dataBase")
    public ResponseEntity<byte[]> downloadAvatar(@PathVariable Long id) {
        Avatar avatar = avatarService.findAvatar(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(avatar.getData().length);

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(avatar.getData());
    }

    @GetMapping(value = "/avatars")
    public ResponseEntity<Collection<Avatar>> listOfAvatars(@RequestParam("page") Integer pageNumber,
                                                            @RequestParam("size") Integer pageSize) {
        return ResponseEntity.ok(avatarService.getAllAvatars(pageNumber, pageSize));
    }

    @GetMapping(value = "/{id}/avatar")
    public void downloadAvatar(@PathVariable Long id, HttpServletResponse response) throws IOException {
        Avatar avatar = avatarService.findAvatar(id);

        Path path = Path.of(avatar.getFilePath());
        try (InputStream is = Files.newInputStream(path);
             OutputStream os = response.getOutputStream();
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            response.setContentType(avatar.getMediaType());
            response.setContentLength((int) avatar.getFileSize());
            response.setStatus(200);
            bis.transferTo(bos);
        }
    }

    @GetMapping(value = "/amount")
    public Long getAmountOfStudents() {
        return studentService.getAmountOfStudents();
    }

    @GetMapping(value = "average-age")
    public Double getAverageAge() {
        return studentService.getAverageAge();
    }

    @GetMapping(value = "/five-last-student")
    public Collection<Student> getFiveLastStudents() {
        return studentService.getFiveLastStudents();
    }

    @PostMapping(value = "/{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> upLoadAvatar(@PathVariable Long id, @RequestParam MultipartFile avatar) throws IOException {
        if (avatar.getSize() > 1024 * 300) {
            logger.warn("Warning: avatar is to big");
            return ResponseEntity.badRequest().body("File is to big");
        }
        avatarService.upLoad(id, avatar);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @PutMapping
    public ResponseEntity<Student> editStudent(@RequestBody Student student) {
        Student editStudent = studentService.editStudent(student);
        if (editStudent == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(student);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Student> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }
}
