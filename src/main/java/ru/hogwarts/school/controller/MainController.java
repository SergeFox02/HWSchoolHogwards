package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hogwarts.school.service.StudentService;

@RestController
public class MainController {

    private final StudentService studentService;

    public MainController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public String helloHogwards() {
        return "Hello yang Mag, in our School";
    }

    @GetMapping("/find-value")
    public int findValue() {
        return studentService.findValue();
    }
}
