package dev.kafetzi.jobmappe.tracking.mapper;

import dev.kafetzi.jobmappe.tracking.model.NoteCategory;
import dev.kafetzi.jobmappe.tracking.model.dto.NoteCategoryRequestDto;
import dev.kafetzi.jobmappe.tracking.model.dto.NoteCategoryDto;

public interface NoteCategoryMapper {
    NoteCategoryDto toDto(NoteCategory noteCategory);
    NoteCategory toEntity(NoteCategoryRequestDto noteCategoryRequestDto);
    void updateEntity(NoteCategory noteCategory, NoteCategoryRequestDto noteCategoryRequestDto);
}
