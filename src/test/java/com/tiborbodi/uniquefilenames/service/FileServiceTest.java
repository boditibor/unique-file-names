package com.tiborbodi.uniquefilenames.service;

import com.tiborbodi.uniquefilenames.entity.FileHistory;
import com.tiborbodi.uniquefilenames.exception.FileOperationException;
import com.tiborbodi.uniquefilenames.repository.FileHistoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {

    @Mock
    private FileHistoryRepository historyRepository;
    @InjectMocks
    private FileService fileService;

    private MockedStatic<Files> filesMock;

    @BeforeEach
    void setUp() {
        filesMock = mockStatic(Files.class, CALLS_REAL_METHODS);
    }

    @AfterEach
    void tearDown() {
        if (filesMock != null) {
            filesMock.close();
        }
    }

    @Test
    void getUniqueFiles_shouldReturnFileCounts() {
        mockStaticFilesIsDirectory(true);
        mockStaticFilesWalk(Stream.of(Path.of("/tmp/file1.txt"), Path.of("/tmp/file2.txt")));
        mockStaticFilesIsRegularFile(true);
        Map<String, Integer> result = fileService.getUniqueFiles("/tmp");
        assertEquals(2, result.size());
    }

    @Test
    void getUniqueFiles_shouldThrowForInvalidPath() {
        mockStaticFilesIsDirectory(false);
        assertThrows(FileOperationException.class, () -> fileService.getUniqueFiles("bad/path"));
    }

    @Test
    void getUniqueFiles_shouldThrowOnIOException() {
        mockStaticFilesIsDirectory(true);
        mockStaticFilesWalkThrowsIOException();
        assertThrows(FileOperationException.class, () -> fileService.getUniqueFiles("/tmp"));
    }

    @Test
    void getHistory_shouldReturnList() {
        FileHistory history = new FileHistory("user", null, "path");
        when(historyRepository.findAll()).thenReturn(List.of(history));
        List<FileHistory> result = fileService.getHistory();
        assertEquals(1, result.size());
    }

    @Test
    void getHistory_shouldReturnEmptyList() {
        when(historyRepository.findAll()).thenReturn(List.of());
        List<FileHistory> result = fileService.getHistory();
        assertTrue(result.isEmpty());
    }

    @Test
    void generateFolderStructure_shouldCreateFoldersAndFiles() {
        List<String> structure = List.of("folder1/file1.txt", "folder2/");
        mockStaticFilesExists(false);
        mockStaticFilesIsDirectory(false);
        mockStaticFilesCreateDirectories();
        mockStaticFilesCreateFile();

        String result = fileService.generateFolderStructure(structure);

        assertEquals("Folder structure created successfully", result);
        filesMock.verify(() -> Files.createFile(any(Path.class)), times(1));
        filesMock.verify(() -> Files.createDirectories(any(Path.class)), times(2));
    }

    @Test
    void generateFolderStructure_shouldSkipExistingFilesAndFolders() {
        List<String> structure = List.of("folder1/file1.txt", "folder2");
        mockStaticFilesExists(true);

        String result = fileService.generateFolderStructure(structure);

        assertEquals("Folder structure created successfully", result);
    }

    @Test
    void generateFolderStructure_shouldThrowForInvalidPath() {
        List<String> structure = List.of("../outside/file.txt");
        mockStaticFilesExists(false);
        mockStaticFilesIsDirectory(false);
        mockStaticFilesCreateDirectories();
        mockStaticFilesCreateFile();

        assertThrows(FileOperationException.class, () -> fileService.generateFolderStructure(structure));
    }

    @Test
    void generateFolderStructure_shouldThrowOnIOException() {
        List<String> structure = List.of("folder1/file1.txt");
        mockStaticFilesExists(false);
        mockStaticFilesIsDirectory(false);
        mockStaticFilesCreateDirectories();
        mockStaticFilesCreateFileThrowsIOException();

        assertThrows(FileOperationException.class, () -> fileService.generateFolderStructure(structure));
    }

    private void mockStaticFilesExists(boolean exists) {
        filesMock.when(() -> Files.exists(any(Path.class))).thenReturn(exists);
    }

    private void mockStaticFilesIsRegularFile(boolean isFile) {
        filesMock.when(() -> Files.isRegularFile(any(Path.class))).thenReturn(isFile);
    }

    private void mockStaticFilesIsDirectory(boolean isDirectory) {
        filesMock.when(() -> Files.isDirectory(any(Path.class))).thenReturn(isDirectory);
    }

    private void mockStaticFilesWalk(Stream<Path> paths) {
        filesMock.when(() -> Files.walk(any())).thenReturn(paths);
    }

    private void mockStaticFilesWalkThrowsIOException() {
        filesMock.when(() -> Files.walk(any())).thenThrow(new IOException("IO error"));
    }

    private void mockStaticFilesCreateDirectories() {
        filesMock.when(() -> Files.createDirectories(any(Path.class))).thenReturn(null);
    }

    private void mockStaticFilesCreateFile() {
        filesMock.when(() -> Files.createFile(any(Path.class))).thenReturn(null);
    }

    private void mockStaticFilesCreateFileThrowsIOException() {
        filesMock.when(() -> Files.createFile(any(Path.class))).thenThrow(new IOException("IO error"));
    }

}

