package ru.hogwarts.school.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.impl.FacultyServiceImpl;

import java.util.Collection;

@RestController
@RequestMapping("/faculty")
public class FacultyController {

    private final String TAG_FACULTY = "Faculty";
    private final FacultyServiceImpl facultyService;
    Logger logger = LoggerFactory.getLogger(FacultyController.class);

    public FacultyController(FacultyServiceImpl facultyService) {
        this.facultyService = facultyService;
    }

    @Operation(
            summary = "Get all faculties in School Hogwarts",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Finding faculties",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Collection.class)
                            )
                    )
            },
            tags = TAG_FACULTY
    )
    @GetMapping
    public ResponseEntity<?> getAllFaculties() {
        logger.info("Call getAllFaculties");
        return ResponseEntity.ok(facultyService.getAllFaculties());
    }

    @Operation(
            summary = "Find faculty by id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Found faculty:",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Faculty.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "If faculty not found",
                            content = @Content(
                                    mediaType = MediaType.TEXT_PLAIN_VALUE,
                                    schema = @Schema(implementation = ResponseEntity.class)
                            )
                    )
            },
            tags = TAG_FACULTY
    )
    @GetMapping("{id}")
    public ResponseEntity<?> findFaculty(@PathVariable Long id) {
        logger.info("Call method findFaculty id = {}", id);
        Faculty findFaculty = facultyService.findFaculty(id);
        if (findFaculty == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(findFaculty);
    }

    @Operation(
            summary = "Filter faculty by color",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Filter faculty by color:",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Faculty.class)
                            )
                    )
            },
            tags = TAG_FACULTY
    )
    @GetMapping("/filter/{color}")
    public ResponseEntity<?> filterFacultiesByColor(@PathVariable String color) {
        logger.info("Call method filterFacultiesByColor color = {}", color);
        return ResponseEntity.ok(facultyService.filterFacultiesByColor(color));
    }

    @Operation(
            summary = "Filter faculty by color and/or name",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Filter faculty by and/or color:",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Faculty.class)
                            )
                    )
            },
            tags = TAG_FACULTY
    )
    @GetMapping("/filter")
    public ResponseEntity<?> filterFacultyByColorOrName(@RequestParam(required = false) String color,
                                                          @RequestParam(required = false) String name) {
        logger.info("Call method filterFacultyByColorOrName color = {}, name = {}", color, name);
        return ResponseEntity.ok(facultyService.filterFacultiesByColorOrName(color, name));
    }

    @Operation(
            summary = "Find students of faculty",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Finding students of faculty",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Collection.class)
                            )
                    )
            },
            tags = TAG_FACULTY
    )
    @GetMapping("/findStudentsOfFaculty/{id}")
    public ResponseEntity<?> findStudentsOfFaculty(@PathVariable Long id) {
        logger.info("Call method findStudentsOfFaculty id = {}", id);
        return ResponseEntity.ok(facultyService.findStudentsOfFaculty(id));
    }

    @Operation(
            summary = "Find longest name of Faculties",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Find longest name of Faculties",
                            content = @Content(
                                    mediaType = MediaType.TEXT_PLAIN_VALUE,
                                    schema = @Schema(implementation = String.class)
                            )
                    )
            },
            tags = TAG_FACULTY
    )
    @GetMapping("/longest-name")
    public ResponseEntity<?> longestNameOfFaculty() {
        logger.info("Call method longestNameOfFaculty");
        return ResponseEntity.ok(facultyService.longestNameOfFaculty());
    }

    @Operation(
            summary = "Create new Faculty",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Information about new Faculty",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Faculty.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Adding Faculty",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Faculty.class)
                            )
                    )
            },
            tags = TAG_FACULTY
    )
    @PostMapping
    public ResponseEntity<?> createFaculty(@RequestBody Faculty faculty) {
        logger.info("Call method createFaculty faculty = {}", faculty );
        return ResponseEntity.ok(facultyService.createFaculty(faculty));
    }

    @Operation(
            summary = "Update information about faculty",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Edit information about faculty",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Faculty.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Update information about faculty",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Faculty.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "If faculty not found in Database, will be received bad request",
                            content = @Content(
                                    mediaType = MediaType.TEXT_PLAIN_VALUE,
                                    schema = @Schema(implementation = ResponseEntity.class)
                            )
                    )
            },
            tags = TAG_FACULTY
    )
    @PutMapping
    public ResponseEntity<?> editFaculty(@RequestBody Faculty faculty) {
        logger.info("Call method editFaculty faculty = {}", faculty );
        Faculty editFaculty = facultyService.editFaculty(faculty);
        if (editFaculty == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(faculty);
    }

    @Operation(
            summary = "Delete faculty",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Faculty is delete from Database",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Faculty.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "if Faculty don't delete, because faculty not found in Database",
                            content = @Content(
                                    mediaType = MediaType.TEXT_PLAIN_VALUE,
                                    schema = @Schema(implementation = ResponseEntity.class)
                            )
                    )
            },
            tags = TAG_FACULTY
    )
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteFaculty(@PathVariable Long id) {
        logger.info("Call method deleteFaculty id = {}", id);
        Faculty deleteFaculty = facultyService.deleteFaculty(id);
        if (deleteFaculty == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(deleteFaculty);
    }

}
