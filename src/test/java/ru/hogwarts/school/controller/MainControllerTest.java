package ru.hogwarts.school.controller;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MainController.class)
class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private MainController mainController;

    @Test
    public void contextLoads(){
        assertThat(mainController).isNotNull();
    }

    @Test
    public void testHelloMessage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("http://localhost:8080/")
                        .accept(MediaType.TEXT_PLAIN_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello yang Mag, in our School"));
    }

}