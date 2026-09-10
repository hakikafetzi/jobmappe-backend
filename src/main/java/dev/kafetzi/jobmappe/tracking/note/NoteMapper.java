package dev.kafetzi.jobmappe.tracking.note;

import dev.kafetzi.jobmappe.tracking.note.dto.NoteCompactDto;
import dev.kafetzi.jobmappe.tracking.note.dto.NoteDetailDto;
import dev.kafetzi.jobmappe.tracking.note.dto.NoteRequestDto;

public interface NoteMapper {

    NoteCompactDto toNoteCompactDto(Note note);
    NoteDetailDto toNoteDetailDto(Note note);
    Note toEntity(NoteRequestDto noteRequestDto);
}
