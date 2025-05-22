package com.tiborbodi.uniquefilenames.controller;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

class JavadocControllerTest {
    @Test
    void serveJavadocIndex_shouldRedirect() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new JavadocController()).build();
        mockMvc.perform(get("/api/v1/doc"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/api/v1/doc/index.html"));
    }
}

