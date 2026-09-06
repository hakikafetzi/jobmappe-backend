package dev.kafetzi.jobmappe.tracking.repository;

import dev.kafetzi.jobmappe.tracking.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface NoteRepository extends JpaRepository<Note, UUID> {

    @Query("SELECT n FROM Note n JOIN FETCH n.noteCategory WHERE n.application.id = :applicationId")
    List<Note> findAllByApplicationIdWithNoteCategory(UUID applicationId);

    @Query("SELECT n FROM Note n JOIN FETCH n.noteCategory WHERE n.id = :noteId")
    Optional<Note> findByIdWithNoteCategory(UUID noteId);
}
