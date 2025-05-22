package com.tiborbodi.uniquefilenames.service;

import com.tiborbodi.uniquefilenames.entity.FileHistory;
import com.tiborbodi.uniquefilenames.exception.FileOperationException;
import com.tiborbodi.uniquefilenames.repository.FileHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Service class for file-related operations such as retrieving unique files,
 * logging history, and generating folder structures.
 */
@Service
public class FileService {

    /**
     * The base path for generated folder structures.
     */
    private static final String GENERATED_BASE_PATH =
            "%s%sgenerated_structure".formatted(System.getProperty("user.dir"), File.separator);
    /**
     * Repository for storing file operation history.
     */
    private final FileHistoryRepository historyRepository;

    /**
     * Constructs a FileService with the given dependencies.
     *
     * @param historyRepository the repository for file history
     */
    @Autowired
    public FileService(FileHistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    /**
     * Retrieves a map of unique file names and their frequency from the specified path.
     *
     * @param path the directory path to scan
     * @return a map of file names to their occurrence frequency
     * @throws FileOperationException if the path is invalid or an I/O error occurs
     */
    public Map<String, Integer> getUniqueFiles(String path) {
        Path location = Paths.get(path);
        if (!Files.isDirectory(location)) {
            throw new FileOperationException("Invalid path: %s".formatted(path));
        }

        Map<String, Integer> fileCounts = new HashMap<>();
        try (Stream<Path> paths = Files.walk(location)) {
            paths.filter(Files::isRegularFile)
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .forEach(fileName -> fileCounts.merge(fileName, 1, Integer::sum));
        } catch (IOException e) {
            throw new FileOperationException("Error traversing directory: %s".formatted(e.getMessage()));
        }

        logHistory(path);
        return fileCounts;
    }

    /**
     * Logs the file operation history for the given path.
     *
     * @param path the path accessed
     */
    private void logHistory(String path) {
        FileHistory history = new FileHistory(
                System.getProperty("user.name"),
                LocalDateTime.now(),
                path
        );
        historyRepository.save(history);
    }

    /**
     * Retrieves the history of file operations.
     *
     * @return a list of file history records
     */
    public List<FileHistory> getHistory() {
        return historyRepository.findAll();
    }

    /**
     * Generates a folder structure based on the provided structure list.
     * <p>
     * Example usage:
     * <pre>
     * [
     *   "file1.txt",
     *   "folder1/",
     *   "folder1/folder2/file2.txt"
     * ]
     * </pre>
     * <p>
     * Each string in the list represents either a file (no trailing slash) or a directory (trailing slash).
     *
     * @param structure the folder/file structure to create
     * @return a success message
     * @throws FileOperationException if an error occurs during creation
     */
    public String generateFolderStructure(List<String> structure) {
        try {
            for (String line : structure) {
                String trimmedLine = line.trim();
                Path filePath = Paths.get(GENERATED_BASE_PATH).resolve(trimmedLine).normalize();

                if (!filePath.startsWith(GENERATED_BASE_PATH)) {
                    throw new FileOperationException("Invalid path: %s".formatted(filePath));
                }

                if (Files.exists(filePath)) {
                    continue;
                }

                boolean shouldBeDirectory = trimmedLine.endsWith("/");
                if (shouldBeDirectory) {
                    Files.createDirectories(filePath);
                } else {
                    Files.createDirectories(filePath.getParent());
                    Files.createFile(filePath);
                }
            }
        } catch (IOException e) {
            throw new FileOperationException("Error creating folder structure: %s".formatted(e.getMessage()));
        }

        return "Folder structure created successfully";
    }
}

