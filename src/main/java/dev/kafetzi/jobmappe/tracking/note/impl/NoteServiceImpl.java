package dev.kafetzi.jobmappe.tracking.note.impl;

import dev.kafetzi.jobmappe.common.pagination.PageableUtils;
import dev.kafetzi.jobmappe.exception.InvalidRequestException;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {
    private final NoteRepository noteRepository;
    private final NoteCategoryRepository noteCategoryRepository;
    private final ApplicationRepository applicationRepository;
    private final NoteMapper noteMapper;

    @Override
    public Page<NoteCompactDto> getAllNotes(String q, UUID noteCategoryId, UUID applicationId, Pageable pageable) {
        Pageable stablePageable = PageableUtils.withStableSort(pageable, Note.class);
        String normalizedQuery = q.strip();
        Page<Note> page;

        if (!normalizedQuery.isEmpty() && (noteCategoryId != null || applicationId != null)) {
            throw new InvalidRequestException("Search is only supported with no filter.");
        }

        if (noteCategoryId != null && applicationId != null) {
            page = noteRepository.findAllByApplicationIdAndNoteCategoryId(applicationId, noteCategoryId, stablePageable);
        }
        else if (noteCategoryId != null) {
            page = noteRepository.findAllByNoteCategoryId(noteCategoryId, stablePageable);
        } else if (applicationId != null) {
            page = noteRepository.findAllByApplicationId(applicationId, stablePageable);
        } else {
            page = noteRepository.findAllWithDetails(normalizedQuery, stablePageable);
        }

        return page.map(noteMapper::toCompactDto);
    }

    @Override
    public NoteDetailDto getNote(UUID id) {
        Note note = noteRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Note with id " + id + " was not found."
                        )
                );

        return noteMapper.toDetailDto(note);
    }

    @Override
    @Transactional
    public NoteDetailDto createNote(NoteRequestDto noteRequestDto) {
        Note note = noteMapper.toEntity(noteRequestDto);

        if (noteRequestDto.applicationId() != null) {
            Application application = applicationRepository.findByIdWithJobPostingAndCompany(noteRequestDto.applicationId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                                    "Application with id " + noteRequestDto.applicationId() + " was not found."
                            )
                    );
            note.setApplication(application);
        }

        if (noteRequestDto.noteCategoryId() != null) {
            NoteCategory noteCategory = noteCategoryRepository.findById(noteRequestDto.noteCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Note category with id " + noteRequestDto.noteCategoryId() + " was not found."
                            )
                    );
            note.setNoteCategory(noteCategory);
        }

        Note savedNote = noteRepository.save(note);
        log.info("Note {} created", savedNote.getId());

        return noteMapper.toDetailDto(savedNote);
    }

    @Override
    @Transactional
    public NoteDetailDto updateNote(UUID id, NoteRequestDto noteRequestDto) {
        Note note = noteRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Note with id " + id + " was not found."
                        )
                );

        if (noteRequestDto.applicationId() != null) {
            Application application = applicationRepository.findByIdWithJobPostingAndCompany(noteRequestDto.applicationId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                                    "Application with id " + noteRequestDto.applicationId() + " was not found."
                            )
                    );
            note.setApplication(application);
        } else {
            note.setApplication(null);
        }

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
        log.info("Note {} updated", note.getId());

        return noteMapper.toDetailDto(note);
    }

    @Override
    @Transactional
    public void deleteNote(UUID id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Note with id " + id + " was not found."
                        )
                );

        noteRepository.delete(note);
        log.info("Note {} deleted", note.getId());
    }
}
