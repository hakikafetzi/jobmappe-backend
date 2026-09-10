package dev.kafetzi.jobmappe.tracking.notecategory;

import dev.kafetzi.jobmappe.tracking.notecategory.dto.NoteCategoryRequestDto;
import dev.kafetzi.jobmappe.tracking.notecategory.dto.NoteCategoryDto;

import java.util.List;
import java.util.UUID;

public interface NoteCategoryService {

    List<NoteCategoryDto> getAllNoteCategories();
    NoteCategoryDto getNoteCategoryById(UUID id);
    NoteCategoryDto createNoteCategory(NoteCategoryRequestDto noteCategoryRequestDto);
    NoteCategoryDto updateNoteCategory(UUID id, NoteCategoryRequestDto noteCategoryRequestDto);
    void deleteNoteCategory(UUID id);
}
