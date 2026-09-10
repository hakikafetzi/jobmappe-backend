package dev.kafetzi.jobmappe.tracking.note;

import dev.kafetzi.jobmappe.tracking.note.dto.NoteCompactDto;
import dev.kafetzi.jobmappe.tracking.note.dto.NoteDetailDto;
import dev.kafetzi.jobmappe.tracking.note.dto.NoteRequestDto;

import java.util.List;
import java.util.UUID;

public interface NoteService {

    List<NoteCompactDto> getAllNotes(UUID applicationId);
    NoteDetailDto getNote(UUID id);
    NoteDetailDto createNote(UUID applicationId, NoteRequestDto noteRequestDto);
    NoteDetailDto updateNote(UUID id, NoteRequestDto noteRequestDto);
}
