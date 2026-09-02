package dev.kafetzi.jobmappe.application.mapper.impl;

import dev.kafetzi.jobmappe.application.mapper.NoteCategoryMapper;
import dev.kafetzi.jobmappe.application.model.NoteCategory;
import dev.kafetzi.jobmappe.application.model.dto.NoteCategoryRequestDto;
import dev.kafetzi.jobmappe.application.model.dto.NoteCategoryDto;
import org.springframework.stereotype.Component;

@Component
public class NoteCategoryMapperImpl implements NoteCategoryMapper {
    @Override
    public NoteCategoryDto toDto(NoteCategory noteCategory) {
        return new NoteCategoryDto(
                noteCategory.getId(),
                noteCategory.getName()
        );
    }

    @Override
    public NoteCategory toEntity(NoteCategoryRequestDto noteCategoryRequestDto) {
        NoteCategory noteCategory = new NoteCategory();

        noteCategory.setName(noteCategoryRequestDto.name());

        return noteCategory;
    }

    @Override
    public void updateEntity(NoteCategory noteCategory, NoteCategoryRequestDto noteCategoryRequestDto) {
        if (noteCategory == null || noteCategoryRequestDto == null) return;

        noteCategory.setName(noteCategoryRequestDto.name());
    }
}
