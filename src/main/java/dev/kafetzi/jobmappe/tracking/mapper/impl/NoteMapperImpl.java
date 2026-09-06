package dev.kafetzi.jobmappe.tracking.mapper.impl;

import dev.kafetzi.jobmappe.tracking.mapper.NoteMapper;
import dev.kafetzi.jobmappe.tracking.model.Note;
import dev.kafetzi.jobmappe.tracking.model.dto.NoteCompactDto;
import dev.kafetzi.jobmappe.tracking.model.dto.NoteDetailDto;
import dev.kafetzi.jobmappe.tracking.model.dto.NoteRequestDto;
import org.springframework.stereotype.Component;

@Component
public class NoteMapperImpl implements NoteMapper {
    @Override
    public NoteCompactDto toNoteCompactDto(Note note) {
        return new NoteCompactDto(
                note.getId(),
                note.getNoteCategory().getName(),
                note.getTitle(),
                note.getCreatedAt()
        );
    }

    @Override
    public NoteDetailDto toNoteDetailDto(Note note) {
        return new NoteDetailDto(
                note.getId(),
                note.getNoteCategory().getName(),
                note.getTitle(),
                note.getContent(),
                note.getCreatedAt(),
                note.getUpdatedAt()
        );
    }

    @Override
    public Note toEntity(NoteRequestDto noteRequestDto) {
        Note note = new Note();
        note.setTitle(noteRequestDto.title());
        note.setContent(noteRequestDto.content());

        return note;
    }
}
