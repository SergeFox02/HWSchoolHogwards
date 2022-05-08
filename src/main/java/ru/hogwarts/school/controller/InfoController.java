package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoController {

    @Autowired
    private ServletWebServerApplicationContext server;

    @GetMapping("/getPort")
    public String getPort() {
        return "Server port = " + server.getWebServer().getPort();
    }
}
