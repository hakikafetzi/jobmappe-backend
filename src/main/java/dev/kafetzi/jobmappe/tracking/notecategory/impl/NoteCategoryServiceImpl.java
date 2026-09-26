package dev.kafetzi.jobmappe.tracking.notecategory.impl;

import dev.kafetzi.jobmappe.tracking.notecategory.NoteCategoryMapper;
import dev.kafetzi.jobmappe.tracking.notecategory.NoteCategory;
import dev.kafetzi.jobmappe.tracking.notecategory.dto.NoteCategoryRequestDto;
import dev.kafetzi.jobmappe.tracking.notecategory.dto.NoteCategoryDto;
import dev.kafetzi.jobmappe.tracking.notecategory.NoteCategoryRepository;
import dev.kafetzi.jobmappe.tracking.notecategory.NoteCategoryService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NoteCategoryServiceImpl implements NoteCategoryService {

    private final NoteCategoryRepository noteCategoryRepository;
    private final NoteCategoryMapper noteCategoryMapper;

    @Override
    public List<NoteCategoryDto> getAllNoteCategories() {
        List<NoteCategory> noteCategories = noteCategoryRepository.findAll();

        return noteCategories
                .stream()
                .map(noteCategoryMapper::toDto)
                .toList();
    }

    @Override
    public NoteCategoryDto getNoteCategoryById(UUID id) {
        NoteCategory noteCategory = noteCategoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Note Category Not Found"));

        return noteCategoryMapper.toDto(noteCategory);
    }

    @Override
    @Transactional
    public NoteCategoryDto createNoteCategory(NoteCategoryRequestDto noteCategoryRequestDto) {
        NoteCategory noteCategory = noteCategoryMapper.toEntity(noteCategoryRequestDto);

        String normalizedCategoryName = noteCategory.getName().strip();

        if (noteCategoryRepository.existsByNameIgnoreCase(normalizedCategoryName)) {
            throw new IllegalArgumentException("Note category already exists with name " + normalizedCategoryName);
        }

        noteCategory.setName(normalizedCategoryName);
        NoteCategory newNoteCategory = noteCategoryRepository.save(noteCategory);

        return noteCategoryMapper.toDto(newNoteCategory);
    }

    @Override
    @Transactional
    public NoteCategoryDto updateNoteCategory(UUID id, NoteCategoryRequestDto noteCategoryRequestDto) {
        NoteCategory noteCategory = noteCategoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Note Category Not Found"));

        String normalizedCategoryName = noteCategoryRequestDto.name().strip();

        if (noteCategoryRepository.existsByNameIgnoreCaseAndIdNot(normalizedCategoryName, id)) {
            throw new IllegalArgumentException("Note category already exists with name " + normalizedCategoryName);
        }

        noteCategoryMapper.updateEntity(noteCategory, noteCategoryRequestDto);
        noteCategory.setName(normalizedCategoryName);

        return noteCategoryMapper.toDto(noteCategory);
    }

    @Override
    @Transactional
    public void deleteNoteCategory(UUID id) {
        NoteCategory noteCategory = noteCategoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Note Category Not Found"));

        noteCategoryRepository.delete(noteCategory);
    }
}
