package com.example.msselevator.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ElevatorControllerIntegrationTest {

    private static final String CONTENT_TYPE = "application/json";

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;


    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void givenWac_whenServletContext_thenItProvidesGreetController() {
        final ServletContext servletContext = webApplicationContext.getServletContext();
        assertNotNull(servletContext);
        assertTrue(servletContext instanceof MockServletContext);
        assertNotNull(webApplicationContext.getBean("elevatorController"));
    }

    @Test
    public void givenElevatorsURI_whenMockMVC_thenResponseOK() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/api/v1/elevators"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(CONTENT_TYPE));
    }

    @Test
    public void givenURIPatchWithPayload_whenMockMVC_thenResponseNoContent() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.patch("/api/v1/elevators"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }


    @Test
    public void givenURIPatchWithPathVariablePayload_whenMockMVC_thenResponseNoContent() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.patch("/api/v1/elevators")
                        .param("id", "1"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
