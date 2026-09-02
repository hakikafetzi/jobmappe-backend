package dev.kafetzi.jobmappe.application.mapper;

import dev.kafetzi.jobmappe.application.model.NoteCategory;
import dev.kafetzi.jobmappe.application.model.dto.NoteCategoryRequestDto;
import dev.kafetzi.jobmappe.application.model.dto.NoteCategoryDto;

public interface NoteCategoryMapper {
    NoteCategoryDto toDto(NoteCategory noteCategory);
    NoteCategory toEntity(NoteCategoryRequestDto noteCategoryRequestDto);
    void updateEntity(NoteCategory noteCategory, NoteCategoryRequestDto noteCategoryRequestDto);
}
