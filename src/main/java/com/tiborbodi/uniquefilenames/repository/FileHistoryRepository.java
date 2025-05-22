package com.tiborbodi.uniquefilenames.repository;

import com.tiborbodi.uniquefilenames.entity.FileHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for FileHistory entities.
 * Provides CRUD operations for file history records.
 */
@Repository
public interface FileHistoryRepository extends JpaRepository<FileHistory, Long> {
}
