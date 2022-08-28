package ru.hogwarts.school.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
import java.util.OptionalDouble;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final String TAG_STUDENT = "Students";
    private final StudentService studentService;
    private final AvatarService avatarService;

    Logger logger = LoggerFactory.getLogger(StudentController.class);

    public StudentController(StudentService studentService, AvatarService avatarService) {
        this.studentService = studentService;
        this.avatarService = avatarService;
    }

    @Operation(
            summary = "Get all students in School Hogwarts",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Found students",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Collection.class)
                            )
                    )
            },
            tags = TAG_STUDENT
    )
    @GetMapping
    public ResponseEntity<Collection<Student>> getAllStudents() {
        logger.info("Call method getAllStudents");
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @Operation(
            summary = "Find student by id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Found student:",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Student.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "If student not found",
                            content = @Content(
                                    mediaType = MediaType.TEXT_PLAIN_VALUE,
                                    schema = @Schema(implementation = ResponseEntity.class)
                            )
                    )
            },
            tags = TAG_STUDENT
    )
    @GetMapping("{id}")
    public ResponseEntity<Student> findStudent(@PathVariable Long id) {
        logger.info("Call method findStudent");
        Student student = studentService.findStudent(id);
        if (student == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(student);
    }

    @Operation(
            summary = "Filter students by age",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Filter students by age:",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Faculty.class)
                            )
                    )
            },
            tags = TAG_STUDENT
    )
    @GetMapping("/filter/{age}")
    public Collection<Student> filterStudentsByAge(@PathVariable Integer age) {
        logger.info("Call method filterStudentsByAge");
        return studentService.filterAgeStudents(age);
    }

    @Operation(
            summary = "Filter students by age between minAge and maxAge",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Filter students by age between minAge and maxAge:",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Faculty.class)
                            )
                    )
            },
            tags = TAG_STUDENT
    )
    @GetMapping("filter")
    public Collection<Student> filterStudentsByAgeBetween(@RequestParam Integer minAge,
                                                          @RequestParam Integer maxAge) {
        logger.info("Call method filterStudentsByAgeBetween");
        return studentService.filterAgeStudents(minAge, maxAge);
    }

    @Operation(
            summary = "Find faculty of Student",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Found faculty of student:",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Faculty.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "If student not found",
                            content = @Content(
                                    mediaType = MediaType.TEXT_PLAIN_VALUE,
                                    schema = @Schema(implementation = ResponseEntity.class)
                            )
                    )
            },
            tags = TAG_STUDENT
    )
    @GetMapping("find-faculty-of-student/{id}")
    public ResponseEntity<Faculty> findFacultyOfStudent(@PathVariable Long id) {
        logger.info("Call method findFacultyOfStudent");
        Faculty faculty = studentService.findFacultyOfStudent(id);
        if (faculty == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(faculty);
    }

    @Operation(
            summary = "Find avatar in Database by id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Found avatar in Database:",
                            content = @Content(
                                    mediaType = MediaType.IMAGE_JPEG_VALUE,
                                    schema = @Schema(implementation = Avatar.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "If avatar not found",
                            content = @Content(
                                    mediaType = MediaType.TEXT_PLAIN_VALUE,
                                    schema = @Schema(implementation = ResponseEntity.class)
                            )
                    )
            },
            tags = TAG_STUDENT
    )
    @GetMapping(value = "/{id}/avatar/dataBase")
    public ResponseEntity<byte[]> downloadAvatar(@PathVariable Long id) {
        logger.info("Call method downloadAvatar");
        Avatar avatar = avatarService.findAvatar(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(avatar.getData().length);

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(avatar.getData());
    }

    @Operation(
            summary = "Get all avatars of students",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Found avatars:",
                            content = @Content(
                                    mediaType = MediaType.IMAGE_JPEG_VALUE,
                                    schema = @Schema(implementation = Avatar.class)
                            )
                    )
            },
            tags = TAG_STUDENT
    )
    @GetMapping(value = "/avatars")
    public ResponseEntity<Collection<Avatar>> listOfAvatars(@RequestParam("page") Integer pageNumber,
                                                            @RequestParam("size") Integer pageSize) {
        logger.info("Call method listOfAvatars");
        return ResponseEntity.ok(avatarService.getAllAvatars(pageNumber, pageSize));
    }

    @Operation(
            summary = "Find avatar by id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Found avatar by id:",
                            content = @Content(
                                    mediaType = MediaType.IMAGE_JPEG_VALUE,
                                    schema = @Schema(implementation = Avatar.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "If avatar not found",
                            content = @Content(
                                    mediaType = MediaType.TEXT_PLAIN_VALUE,
                                    schema = @Schema(implementation = ResponseEntity.class)
                            )
                    )
            },
            tags = TAG_STUDENT
    )
    @GetMapping(value = "/{id}/avatar")
    public void downloadAvatar(@PathVariable Long id, HttpServletResponse response) throws IOException {
        logger.info("Call method downloadAvatar");
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

    @Operation(
            summary = "Get amount of students",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Amount of students:",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Long.class)
                            )
                    )
            },
            tags = TAG_STUDENT
    )
    @GetMapping(value = "/amount")
    public Long getAmountOfStudents() {
        logger.info("Call method getAmountOfStudents");
        return studentService.getAmountOfStudents();
    }

    @Operation(
            summary = "Calculate average age of students",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Average age of students:",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Double.class)
                            )
                    )
            },
            tags = TAG_STUDENT
    )
    @GetMapping(value = "average-age")
    public Double getAverageAge() {
        logger.info("Call method getAverageAge");
        return studentService.getAverageAge();
    }

    @Operation(
            summary = "Get five last students",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Five last students",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Collection.class)
                            )
                    )
            },
            tags = TAG_STUDENT
    )
    @GetMapping(value = "/five-last-student")
    public Collection<Student> getFiveLastStudents() {
        logger.info("Call method getFiveLastStudents");
        return studentService.getFiveLastStudents();
    }

    @Operation(
            summary = "filter students whose name starts with the letter \"A\"",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "students whose name starts with the letter \"A\"",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Collection.class)
                            )
                    )
            },
            tags = TAG_STUDENT
    )
    @GetMapping(value = "/filter-start-with-A")
    public Collection<String> filterStudentsStartNameWithA() {
        logger.info("Call method filterStudentsStartNameWithA");
        return studentService.filterStudentsBuOrderStartNameA();
    }

    @Operation(
            summary = "Calculate average age of students by Stream",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Average age of students:",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Double.class)
                            )
                    )
            },
            tags = TAG_STUDENT
    )
    @GetMapping(value = "/average-age-by-stream")
    public ResponseEntity<OptionalDouble> getAverageAgeByStream() {
        logger.info("Call method getAverageAgeByStream");
        return ResponseEntity.ok(studentService.averageAge());
    }

    @Operation(
            summary = "Add avatar of student",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Added avatar",
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Added avatar",
                            content = @Content(
                                    mediaType = MediaType.IMAGE_JPEG_VALUE
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "If added photo is to big",
                            content = @Content(
                                    mediaType = MediaType.TEXT_PLAIN_VALUE,
                                    schema = @Schema(implementation = ResponseEntity.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "If not found student",
                            content = @Content(
                                    mediaType = MediaType.TEXT_PLAIN_VALUE,
                                    schema = @Schema(implementation = ResponseEntity.class)
                            )
                    )
            },
            tags = TAG_STUDENT
    )
    @PostMapping(value = "/{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> upLoadAvatar(@PathVariable Long id, @RequestParam MultipartFile avatar) throws IOException {
        logger.info("Call method upLoadAvatar");
        if (avatar.getSize() > 1024 * 300) {
            logger.warn("Warning: avatar is to big");
            return ResponseEntity.badRequest().body("File is to big");
        }
        avatarService.upLoad(id, avatar);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Create new Student",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Information about new Student",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Faculty.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Adding Student",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Faculty.class)
                            )
                    )
            },
            tags = TAG_STUDENT
    )
    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        logger.info("Call method createStudent");
        return studentService.createStudent(student);
    }

    @Operation(
            summary = "Update information about student",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Edit information about student",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Student.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Update information about student",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Student.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "If student not found in Database, will be received bad request",
                            content = @Content(
                                    mediaType = MediaType.TEXT_PLAIN_VALUE,
                                    schema = @Schema(implementation = ResponseEntity.class)
                            )
                    )
            },
            tags = TAG_STUDENT
    )
    @PutMapping
    public ResponseEntity<Student> editStudent(@RequestBody Student student) {
        logger.info("Call method editStudent");
        Student editStudent = studentService.editStudent(student);
        if (editStudent == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(student);
    }

    @Operation(
            summary = "Delete student",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Student is delete from Database",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Student.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "If student don't delete, because student not found in Database",
                            content = @Content(
                                    mediaType = MediaType.TEXT_PLAIN_VALUE,
                                    schema = @Schema(implementation = ResponseEntity.class)
                            )
                    )
            },
            tags = TAG_STUDENT
    )
    @DeleteMapping("{id}")
    public ResponseEntity<Student> deleteStudent(@PathVariable Long id) {
        logger.info("Call method deleteStudent");
        Student deleteStudent = studentService.deleteStudent(id);
        if (deleteStudent == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(deleteStudent);
    }

}
