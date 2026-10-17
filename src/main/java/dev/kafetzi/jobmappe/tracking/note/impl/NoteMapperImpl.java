package dev.kafetzi.jobmappe.tracking.note.impl;

import dev.kafetzi.jobmappe.tracking.note.NoteMapper;
import dev.kafetzi.jobmappe.tracking.note.Note;
import dev.kafetzi.jobmappe.tracking.note.dto.NoteCompactDto;
import dev.kafetzi.jobmappe.tracking.note.dto.NoteDetailDto;
import dev.kafetzi.jobmappe.tracking.note.dto.NoteRequestDto;
import org.springframework.stereotype.Component;

@Component
public class NoteMapperImpl implements NoteMapper {
    @Override
    public NoteCompactDto toNoteCompactDto(Note note) {
        return new NoteCompactDto(
                note.getId(),
                note.getNoteCategory() != null ? note.getNoteCategory().getName() : null,
                note.getTitle(),
                note.getCreatedAt(),
                note.getUpdatedAt(),
                note.getApplication() != null ? note.getApplication().getId() : null,
                note.getApplication() != null ? note.getApplication().getJobPosting().getTitle() : null,
                note.getApplication() != null ? note.getApplication().getJobPosting().getCompany().getName() : null
        );
    }

    @Override
    public NoteDetailDto toNoteDetailDto(Note note) {
        return new NoteDetailDto(
                note.getId(),
                note.getNoteCategory() != null ? note.getNoteCategory().getName() : null,
                note.getTitle(),
                note.getContent(),
                note.getCreatedAt(),
                note.getUpdatedAt(),
                note.getApplication() != null ? note.getApplication().getId() : null,
                note.getApplication() != null ? note.getApplication().getJobPosting().getTitle() : null,
                note.getApplication() != null ? note.getApplication().getJobPosting().getCompany().getName() : null
        );
    }

    @Override
    public Note toEntity(NoteRequestDto noteRequestDto) {
        Note note = new Note();
        note.setTitle(noteRequestDto.title());
        note.setContent(noteRequestDto.content());

        return note;
    }

    @Override
    public void updateEntityFromDto(NoteRequestDto noteRequestDto, Note note) {
        if (noteRequestDto == null || note == null) return;

        note.setTitle(noteRequestDto.title());
        note.setContent(noteRequestDto.content());
    }
}
