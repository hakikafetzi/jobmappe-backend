package dev.kafetzi.jobmappe.tracking.service;

import dev.kafetzi.jobmappe.tracking.model.dto.NoteCompactDto;
import dev.kafetzi.jobmappe.tracking.model.dto.NoteDetailDto;
import dev.kafetzi.jobmappe.tracking.model.dto.NoteRequestDto;

import java.util.List;
import java.util.UUID;

public interface NoteService {

    List<NoteCompactDto> getAllNotes(UUID applicationId);
    NoteDetailDto getNote(UUID id);
    NoteDetailDto createNote(UUID applicationId, NoteRequestDto noteRequestDto);
}
