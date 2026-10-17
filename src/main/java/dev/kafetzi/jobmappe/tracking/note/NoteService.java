package dev.kafetzi.jobmappe.tracking.note;

import dev.kafetzi.jobmappe.tracking.note.dto.NoteCompactDto;
import dev.kafetzi.jobmappe.tracking.note.dto.NoteDetailDto;
import dev.kafetzi.jobmappe.tracking.note.dto.NoteRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface NoteService {
    Page<NoteCompactDto> getAllNotes(String q, UUID noteCategoryId, UUID applicationId, Pageable pageable);
    NoteDetailDto getNote(UUID id);
    NoteDetailDto createNote(NoteRequestDto noteRequestDto);
    NoteDetailDto updateNote(UUID id, NoteRequestDto noteRequestDto);
    void deleteNote(UUID id);
}
