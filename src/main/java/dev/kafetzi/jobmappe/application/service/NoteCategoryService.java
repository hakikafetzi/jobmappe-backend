package dev.kafetzi.jobmappe.application.service;

import dev.kafetzi.jobmappe.application.model.dto.NoteCategoryRequestDto;
import dev.kafetzi.jobmappe.application.model.dto.NoteCategoryDto;

import java.util.List;
import java.util.UUID;

public interface NoteCategoryService {

    List<NoteCategoryDto> getAllNoteCategories();
    NoteCategoryDto getNoteCategoryById(UUID id);
    NoteCategoryDto createNoteCategory(NoteCategoryRequestDto noteCategoryRequestDto);
    NoteCategoryDto updateNoteCategory(UUID id, NoteCategoryRequestDto noteCategoryRequestDto);
    void deleteNoteCategory(UUID id);
}
