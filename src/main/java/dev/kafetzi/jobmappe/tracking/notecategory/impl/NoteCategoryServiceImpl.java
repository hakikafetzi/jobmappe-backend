package dev.kafetzi.jobmappe.tracking.notecategory.impl;

import dev.kafetzi.jobmappe.exception.ConflictException;
import dev.kafetzi.jobmappe.exception.ResourceNotFoundException;
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
        List<NoteCategory> noteCategories = noteCategoryRepository.findAllByOrderByNameAsc();

        return noteCategories
                .stream()
                .map(noteCategoryMapper::toDto)
                .toList();
    }

    @Override
    public NoteCategoryDto getNoteCategoryById(UUID id) {
        NoteCategory noteCategory = noteCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Note category with id " + id + " was not found."
                        )
                );

        return noteCategoryMapper.toDto(noteCategory);
    }

    @Override
    @Transactional
    public NoteCategoryDto createNoteCategory(NoteCategoryRequestDto noteCategoryRequestDto) {
        NoteCategory noteCategory = noteCategoryMapper.toEntity(noteCategoryRequestDto);

        if (noteCategoryRepository.existsByNameIgnoreCase(noteCategory.getName())) {
            throw new ConflictException(
                    "Note category with name '" + noteCategory.getName() + "' already exists."
            );
        }

        NoteCategory newNoteCategory = noteCategoryRepository.save(noteCategory);

        return noteCategoryMapper.toDto(newNoteCategory);
    }

    @Override
    @Transactional
    public NoteCategoryDto updateNoteCategory(UUID id, NoteCategoryRequestDto noteCategoryRequestDto) {
        NoteCategory noteCategory = noteCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Note category with id " + id + " was not found."
                        )
                );

        if (noteCategoryRepository.existsByNameIgnoreCaseAndIdNot(noteCategoryRequestDto.name(), id)) {
            throw new ConflictException(
                    "Note category with name '" + noteCategoryRequestDto.name() + "' already exists."
            );
        }

        noteCategoryMapper.updateEntityFromDto(noteCategoryRequestDto, noteCategory);

        return noteCategoryMapper.toDto(noteCategory);
    }

    @Override
    @Transactional
    public void deleteNoteCategory(UUID id) {
        NoteCategory noteCategory = noteCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Note category with id " + id + " was not found."
                        )
                );

        noteCategoryRepository.delete(noteCategory);
    }
}
