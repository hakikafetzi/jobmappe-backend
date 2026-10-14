package dev.kafetzi.jobmappe.tracking.note;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface NoteRepository extends JpaRepository<Note, UUID> {
    @Query("""
            SELECT n FROM Note n
            WHERE n.application.id = :applicationId
            """)
    @EntityGraph(attributePaths = "noteCategory")
    List<Note> findAllByApplicationIdWithNoteCategory(@Param("applicationId") UUID applicationId);

    @Query("""
            SELECT n FROM Note n
            WHERE n.id = :noteId
            """)
    @EntityGraph(attributePaths = "noteCategory")
    Optional<Note> findByIdWithNoteCategory(@Param("noteId") UUID noteId);
}
