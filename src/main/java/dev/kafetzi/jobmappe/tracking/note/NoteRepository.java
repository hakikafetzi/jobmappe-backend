package dev.kafetzi.jobmappe.tracking.note;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface NoteRepository extends JpaRepository<Note, UUID> {

    @Query("SELECT n FROM Note n LEFT JOIN FETCH n.noteCategory WHERE n.application.id = :applicationId")
    List<Note> findAllByApplicationIdWithNoteCategory(@Param("applicationId") UUID applicationId);

    @Query("SELECT n FROM Note n LEFT JOIN FETCH n.noteCategory WHERE n.id = :noteId")
    Optional<Note> findByIdWithNoteCategory(@Param("noteId") UUID noteId);
}
