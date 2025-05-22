package com.tiborbodi.uniquefilenames.controller;

import com.tiborbodi.uniquefilenames.entity.FileHistory;
import com.tiborbodi.uniquefilenames.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller for file-related operations such as retrieving unique files, history, and generating folder structures.
 */
@RestController
@RequestMapping("/api/v1")
public class FileController {

    private final FileService fileService;

    /**
     * Constructs a FileController with the given FileService.
     *
     * @param fileService the file service to use
     */
    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * Retrieves a map of unique file names and their counts from the specified path.
     *
     * @param path the directory path to scan
     * @return a map of file names to their occurrence counts
     */
    @GetMapping("/get_unique")
    public Map<String, Integer> getUnique(@RequestParam String path) {
        return fileService.getUniqueFiles(path);
    }

    /**
     * Retrieves the history of file operations.
     *
     * @return a list of file history records
     */
    @GetMapping("/history")
    public List<FileHistory> getHistory() {
        return fileService.getHistory();
    }

    /**
     * Generates a folder structure based on the provided structure list.
     * <p>
     * Example request body:
     * <pre>
     * [
     *   "file1.txt",
     *   "folder1/",
     *   "folder1/folder2/file2.txt"
     * ]
     * </pre>
     *
     * @param structure the folder/file structure to create
     * @return a success message
     */
    @PostMapping("/gen")
    public String generateStructure(@RequestBody List<String> structure) {
        return fileService.generateFolderStructure(structure);
    }

}
