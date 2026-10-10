package dev.kafetzi.jobmappe.tracking.note.impl;

import dev.kafetzi.jobmappe.exception.ResourceNotFoundException;
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
        if (!applicationRepository.existsById(applicationId)) {
            throw new ResourceNotFoundException(
                    "Application with id " + applicationId + " was not found."
            );
        }

        return noteRepository.findAllByApplicationIdWithNoteCategory(applicationId)
                .stream()
                .map(noteMapper::toNoteCompactDto)
                .toList();
    }

    @Override
    public NoteDetailDto getNote(UUID id) {
        Note note = noteRepository.findByIdWithNoteCategory(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Note with id " + id + " was not found."
                        )
                );

        return noteMapper.toNoteDetailDto(note);
    }

    @Override
    @Transactional
    public NoteDetailDto createNote(UUID applicationId, NoteRequestDto noteRequestDto) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                                "Application with id " + applicationId + " was not found."
                        )
                );

        Note note = noteMapper.toEntity(noteRequestDto, application);

        if (noteRequestDto.noteCategoryId() != null) {
            NoteCategory noteCategory = noteCategoryRepository.findById(noteRequestDto.noteCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Note category with id " + noteRequestDto.noteCategoryId() + " was not found."
                            )
                    );
            note.setNoteCategory(noteCategory);
        }

        Note savedNote = noteRepository.save(note);

        return noteMapper.toNoteDetailDto(savedNote);
    }

    @Override
    @Transactional
    public NoteDetailDto updateNote(UUID id, NoteRequestDto noteRequestDto) {
        Note note = noteRepository.findByIdWithNoteCategory(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Note with id " + id + " was not found."
                        )
                );

        if (noteRequestDto.noteCategoryId() != null) {
            NoteCategory noteCategory = noteCategoryRepository.findById(noteRequestDto.noteCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Note category with id " + noteRequestDto.noteCategoryId() + " was not found."
                            )
                    );
            note.setNoteCategory(noteCategory);
        } else {
            note.setNoteCategory(null);
        }

        noteMapper.updateEntityFromDto(noteRequestDto, note);

        return noteMapper.toNoteDetailDto(note);
    }
}
