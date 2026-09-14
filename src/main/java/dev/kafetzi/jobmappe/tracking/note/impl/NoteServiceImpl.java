package dev.kafetzi.jobmappe.tracking.note.impl;

import dev.kafetzi.jobmappe.tracking.note.NoteMapper;
import dev.kafetzi.jobmappe.tracking.application.Application;
import dev.kafetzi.jobmappe.tracking.note.Note;
import dev.kafetzi.jobmappe.tracking.notecategory.NoteCategory;
import dev.kafetzi.jobmappe.tracking.note.dto.NoteCompactDto;
import dev.kafetzi.jobmappe.tracking.note.dto.NoteDetailDto;
import dev.kafetzi.jobmappe.tracking.note.dto.NoteRequestDto;
import dev.kafetzi.jobmappe.tracking.application.ApplicationRepository;
import dev.kafetzi.jobmappe.tracking.notecategory.NoteCategoryRepository;
import dev.kafetzi.jobmappe.tracking.note.NoteRepository;
import dev.kafetzi.jobmappe.tracking.note.NoteService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
    private final NoteCategoryRepository noteCategoryRepository;
    private final ApplicationRepository applicationRepository;
    private final NoteMapper noteMapper;

    @Override
    public List<NoteCompactDto> getAllNotes(UUID applicationId) {
        return noteRepository.findAllByApplicationIdWithNoteCategory(applicationId)
                .stream()
                .map(noteMapper::toNoteCompactDto)
                .toList();
    }

    @Override
    public NoteDetailDto getNote(UUID id) {
        Note note = noteRepository.findByIdWithNoteCategory(id)
                .orElseThrow(() -> new IllegalArgumentException("Note not found with id: " + id));

        return noteMapper.toNoteDetailDto(note);
    }

    @Override
    @Transactional
    public NoteDetailDto createNote(UUID applicationId, NoteRequestDto noteRequestDto) {
        Note note = noteMapper.toEntity(noteRequestDto);

        if ((noteRequestDto.noteCategoryId() != null)) {
            NoteCategory noteCategory = noteCategoryRepository.findById(noteRequestDto.noteCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Note category not found with id: " + noteRequestDto.noteCategoryId()));
            note.setNoteCategory(noteCategory);
        }

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found with id: " + applicationId));

        note.setApplication(application);

        Note savedNote = noteRepository.save(note);

        return noteMapper.toNoteDetailDto(savedNote);
    }

    @Override
    @Transactional
    public NoteDetailDto updateNote(UUID id, NoteRequestDto noteRequestDto) {
        Note note = noteRepository.findByIdWithNoteCategory(id)
                .orElseThrow(() -> new IllegalArgumentException("Note not found with id: " + id));

        if (noteRequestDto.noteCategoryId() != null) {
            NoteCategory noteCategory = noteCategoryRepository.findById(noteRequestDto.noteCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Note category not found with id: " + noteRequestDto.noteCategoryId()));
            note.setNoteCategory(noteCategory);
        } else {
            note.setNoteCategory(null);
        }

        noteMapper.updateEntityFromDto(noteRequestDto, note);

        return noteMapper.toNoteDetailDto(note);
    }
}
