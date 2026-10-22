package dev.kafetzi.jobmappe.tracking.notecategory.impl;

import dev.kafetzi.jobmappe.exception.ConflictException;
import dev.kafetzi.jobmappe.tracking.notecategory.NoteCategory;
import dev.kafetzi.jobmappe.tracking.notecategory.NoteCategoryMapper;
import dev.kafetzi.jobmappe.tracking.notecategory.NoteCategoryRepository;
import dev.kafetzi.jobmappe.tracking.notecategory.dto.NoteCategoryRequestDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NoteCategoryServiceImplTest {
    @Mock NoteCategoryRepository noteCategoryRepository;
    @Mock NoteCategoryMapper noteCategoryMapper;
    @InjectMocks NoteCategoryServiceImpl noteCategoryService;

    @Test
    void createNoteCategory_nameAlreadyExists_throwsConflict() {
        NoteCategoryRequestDto requestDto = new NoteCategoryRequestDto("test");
        NoteCategory noteCategory = new NoteCategory();
        noteCategory.setName("test");
        when(noteCategoryMapper.toEntity(requestDto)).thenReturn(noteCategory);
        when(noteCategoryRepository.existsByNameIgnoreCase(noteCategory.getName())).thenReturn(true);

        assertThatThrownBy(() -> noteCategoryService.createNoteCategory(requestDto))
                .isInstanceOf(ConflictException.class);
        verify(noteCategoryRepository, never()).save(any());
    }

    @Test
    void updateNoteCategory_nameUsedByOtherNoteCategory_throwsConflict() {
        UUID noteCategoryId = UUID.randomUUID();
        NoteCategoryRequestDto requestDto = new NoteCategoryRequestDto("test");
        when(noteCategoryRepository.findById(noteCategoryId)).thenReturn(Optional.of(new NoteCategory()));
        when(noteCategoryRepository.existsByNameIgnoreCaseAndIdNot(requestDto.name(), noteCategoryId)).thenReturn(true);

        assertThatThrownBy(() -> noteCategoryService.updateNoteCategory(noteCategoryId, requestDto))
                .isInstanceOf(ConflictException.class);
        verify(noteCategoryMapper, never()).updateEntityFromDto(any(), any());
    }
}