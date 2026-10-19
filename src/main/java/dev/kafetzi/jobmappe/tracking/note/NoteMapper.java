package dev.kafetzi.jobmappe.tracking.note;

import dev.kafetzi.jobmappe.tracking.note.dto.NoteCompactDto;
import dev.kafetzi.jobmappe.tracking.note.dto.NoteDetailDto;
import dev.kafetzi.jobmappe.tracking.note.dto.NoteRequestDto;

public interface NoteMapper {
    NoteCompactDto toCompactDto(Note note);
    NoteDetailDto toDetailDto(Note note);
    Note toEntity(NoteRequestDto noteRequestDto);
    void updateEntityFromDto(NoteRequestDto noteRequestDto, Note note);
}
