package dev.kafetzi.jobmappe.tracking.note.impl;

import dev.kafetzi.jobmappe.exception.InvalidRequestException;
import dev.kafetzi.jobmappe.exception.ResourceNotFoundException;
import dev.kafetzi.jobmappe.tracking.application.ApplicationRepository;
import dev.kafetzi.jobmappe.tracking.note.NoteMapper;
import dev.kafetzi.jobmappe.tracking.note.NoteRepository;
import dev.kafetzi.jobmappe.tracking.note.dto.NoteRequestDto;
import dev.kafetzi.jobmappe.tracking.notecategory.NoteCategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NoteServiceImplTest {
    @Mock NoteRepository noteRepository;
    @Mock NoteCategoryRepository noteCategoryRepository;
    @Mock ApplicationRepository applicationRepository;
    @Mock NoteMapper noteMapper;
    @InjectMocks NoteServiceImpl noteService;

    static Stream<Arguments> filterCombinations() {
        return Stream.of(
                Arguments.of(UUID.randomUUID(), null),
                Arguments.of(null, UUID.randomUUID()),
                Arguments.of(UUID.randomUUID(), UUID.randomUUID())
        );
    }

    @ParameterizedTest
    @MethodSource("filterCombinations")
    void getAllNotes_searchWithFilters_throwsInvalidRequest(UUID noteCategoryId, UUID applicationId) {
        Pageable pageable = PageRequest.of(0, 20, Sort.by("title"));
        String q = "test";

        assertThatThrownBy(() -> noteService.getAllNotes(q, noteCategoryId, applicationId, pageable))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage("Search is only supported with no filter.");
    }

    @Test
    void createNote_unknownApplication_throwsNotFound() {
        UUID applicationId = UUID.randomUUID();
        NoteRequestDto noteRequestDto = new NoteRequestDto(applicationId, null, "Title", null);
        when(applicationRepository.findByIdWithJobPostingAndCompany(applicationId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> noteService.createNote(noteRequestDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Application with id " + applicationId + " was not found.");

        verify(noteRepository, never()).save(any());
    }

    @Test
    void createNote_unknownNoteCategory_throwsNotFound() {
        UUID noteCategoryId = UUID.randomUUID();
        NoteRequestDto noteRequestDto = new NoteRequestDto(null, noteCategoryId, "Title", null);
        when(noteCategoryRepository.findById(noteCategoryId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> noteService.createNote(noteRequestDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Note category with id " + noteCategoryId + " was not found.");

        verify(noteRepository, never()).save(any());
    }
}