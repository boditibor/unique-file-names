package com.tiborbodi.uniquefilenames.controller;

import com.tiborbodi.uniquefilenames.entity.FileHistory;
import com.tiborbodi.uniquefilenames.exception.FileOperationException;
import com.tiborbodi.uniquefilenames.exception.GlobalExceptionHandler;
import com.tiborbodi.uniquefilenames.service.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class FileControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FileService fileService;

    @InjectMocks
    private FileController fileController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(fileController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getUnique_shouldReturnFileCounts() throws Exception {
        when(fileService.getUniqueFiles(anyString())).thenReturn(Map.of("file1.txt", 2));
        mockMvc.perform(get("/api/v1/get_unique").param("path", "some/path"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['file1.txt']").value(2));
    }

    @Test
    void getUnique_shouldReturnErrorForInvalidPath() throws Exception {
        when(fileService.getUniqueFiles(anyString())).thenThrow(new FileOperationException("Invalid path"));
        mockMvc.perform(get("/api/v1/get_unique").param("path", "bad/path"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getHistory_shouldReturnHistoryList() throws Exception {
        FileHistory history = new FileHistory("user", null, "path");
        when(fileService.getHistory()).thenReturn(List.of(history));
        mockMvc.perform(get("/api/v1/history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userName").value("user"));
    }

    @Test
    void getHistory_shouldReturnEmptyList() throws Exception {
        when(fileService.getHistory()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/v1/history"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void generateStructure_shouldReturnSuccess() throws Exception {
        when(fileService.generateFolderStructure(any())).thenReturn("Success");
        mockMvc.perform(post("/api/v1/gen")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[\"folder1\",\"folder2\"]"))
                .andExpect(status().isOk())
                .andExpect(content().string("Success"));
    }

    @Test
    void generateStructure_shouldReturnErrorOnFailure() throws Exception {
        when(fileService.generateFolderStructure(any())).thenThrow(new FileOperationException("Failed"));
        mockMvc.perform(post("/api/v1/gen")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[\"folder1\"]"))
                .andExpect(status().isInternalServerError());
    }
}

