package dev.kafetzi.jobmappe.tracking.service.impl;

import dev.kafetzi.jobmappe.tracking.mapper.NoteCategoryMapper;
import dev.kafetzi.jobmappe.tracking.model.NoteCategory;
import dev.kafetzi.jobmappe.tracking.model.dto.NoteCategoryRequestDto;
import dev.kafetzi.jobmappe.tracking.model.dto.NoteCategoryDto;
import dev.kafetzi.jobmappe.tracking.repository.NoteCategoryRepository;
import dev.kafetzi.jobmappe.tracking.service.NoteCategoryService;
import jakarta.transaction.Transactional;
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

        if (noteCategoryRepository.existsByNameIgnoreCase(noteCategory.getName())) {
            throw new IllegalArgumentException("Note category already exists with name " + noteCategory.getName());
        }

        NoteCategory newNoteCategory = noteCategoryRepository.save(noteCategory);

        return noteCategoryMapper.toDto(newNoteCategory);
    }

    @Override
    @Transactional
    public NoteCategoryDto updateNoteCategory(UUID id, NoteCategoryRequestDto noteCategoryRequestDto) {
        NoteCategory noteCategory = noteCategoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Note Category Not Found"));

        noteCategoryMapper.updateEntity(noteCategory, noteCategoryRequestDto);

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
