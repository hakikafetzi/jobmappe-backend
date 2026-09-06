package dev.kafetzi.jobmappe.tracking.service.impl;

import dev.kafetzi.jobmappe.tracking.mapper.NoteMapper;
import dev.kafetzi.jobmappe.tracking.model.Application;
import dev.kafetzi.jobmappe.tracking.model.Note;
import dev.kafetzi.jobmappe.tracking.model.NoteCategory;
import dev.kafetzi.jobmappe.tracking.model.dto.NoteCompactDto;
import dev.kafetzi.jobmappe.tracking.model.dto.NoteDetailDto;
import dev.kafetzi.jobmappe.tracking.model.dto.NoteRequestDto;
import dev.kafetzi.jobmappe.tracking.repository.ApplicationRepository;
import dev.kafetzi.jobmappe.tracking.repository.NoteCategoryRepository;
import dev.kafetzi.jobmappe.tracking.repository.NoteRepository;
import dev.kafetzi.jobmappe.tracking.service.NoteService;
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
    public NoteDetailDto createNote(UUID applicationId, NoteRequestDto noteRequestDto) {
        NoteCategory noteCategory = noteCategoryRepository.findById(noteRequestDto.noteCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Note category not found with id: " + noteRequestDto.noteCategoryId()));
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found with id: " + applicationId));

        Note note = noteMapper.toEntity(noteRequestDto);
        note.setNoteCategory(noteCategory);
        note.setApplication(application);

        Note savedNote = noteRepository.save(note);

        return noteMapper.toNoteDetailDto(savedNote);
    }
}
