package dev.kafetzi.jobmappe.tracking.notecategory;

import dev.kafetzi.jobmappe.tracking.notecategory.dto.NoteCategoryRequestDto;
import dev.kafetzi.jobmappe.tracking.notecategory.dto.NoteCategoryDto;

public interface NoteCategoryMapper {
    NoteCategoryDto toDto(NoteCategory noteCategory);
    NoteCategory toEntity(NoteCategoryRequestDto noteCategoryRequestDto);
    void updateEntity(NoteCategory noteCategory, NoteCategoryRequestDto noteCategoryRequestDto);
}
