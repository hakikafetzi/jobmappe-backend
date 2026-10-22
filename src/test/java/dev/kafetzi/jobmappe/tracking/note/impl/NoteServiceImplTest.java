package dev.kafetzi.jobmappe.tracking.note.impl;

import dev.kafetzi.jobmappe.exception.InvalidRequestException;
import dev.kafetzi.jobmappe.tracking.application.ApplicationRepository;
import dev.kafetzi.jobmappe.tracking.note.NoteMapper;
import dev.kafetzi.jobmappe.tracking.note.NoteRepository;
import dev.kafetzi.jobmappe.tracking.notecategory.NoteCategoryRepository;
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

import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
}