package dev.kafetzi.jobmappe.tracking.mapper;

import dev.kafetzi.jobmappe.tracking.model.Note;
import dev.kafetzi.jobmappe.tracking.model.dto.NoteCompactDto;
import dev.kafetzi.jobmappe.tracking.model.dto.NoteDetailDto;
import dev.kafetzi.jobmappe.tracking.model.dto.NoteRequestDto;

public interface NoteMapper {

    NoteCompactDto toNoteCompactDto(Note note);
    NoteDetailDto toNoteDetailDto(Note note);
    Note toEntity(NoteRequestDto noteRequestDto);
}
