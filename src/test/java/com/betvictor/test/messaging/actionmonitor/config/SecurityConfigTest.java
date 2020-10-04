package com.betvictor.test.messaging.actionmonitor.config;

import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class SecurityConfigTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void checkActuatorIsNotProtected() throws Exception {
        mockMvc.perform(get("/actuator/health").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        mockMvc.perform(get("/actuator/info").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    void checkPrivateEndpointsAreProtected() throws Exception {
        mockMvc.perform(get("/private/" + RandomString.make()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isFound()).andExpect(redirectedUrl("http://localhost/login.html"));
    }

    @Test
    void checkLoginWorks() throws Exception {
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED).
                content("username=testUser&password=testPass")
        ).andExpect(status().isFound()).andExpect(redirectedUrl("/private/home.html"));
    }
}