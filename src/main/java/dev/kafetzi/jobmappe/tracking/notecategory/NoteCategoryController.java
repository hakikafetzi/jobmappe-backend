package dev.kafetzi.jobmappe.tracking.notecategory;

import dev.kafetzi.jobmappe.tracking.notecategory.dto.NoteCategoryRequestDto;
import dev.kafetzi.jobmappe.tracking.notecategory.dto.NoteCategoryDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/note-categories")
@RequiredArgsConstructor
public class NoteCategoryController {

    private final NoteCategoryService noteCategoryService;

    @GetMapping
    public ResponseEntity<List<NoteCategoryDto>> getAllNoteCategories() {
        List<NoteCategoryDto>  noteCategories = noteCategoryService.getAllNoteCategories();

        return ResponseEntity.ok(noteCategories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteCategoryDto> getNoteCategory(@PathVariable UUID id) {
        NoteCategoryDto noteCategory = noteCategoryService.getNoteCategoryById(id);

        return ResponseEntity.ok(noteCategory);
    }

    @PostMapping
    public ResponseEntity<NoteCategoryDto> createNoteCategory(
            @Valid @RequestBody NoteCategoryRequestDto noteCategoryRequestDto
    ) {
        NoteCategoryDto noteCategoryDto = noteCategoryService.createNoteCategory(noteCategoryRequestDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/companies/{id}")
                .buildAndExpand(noteCategoryDto.id())
                .toUri();

        return ResponseEntity.created(location).body(noteCategoryDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoteCategoryDto> updateNoteCategory(
            @PathVariable UUID id,
            @Valid @RequestBody NoteCategoryRequestDto noteCategoryRequestDto
    ) {
        NoteCategoryDto noteCategoryDto = noteCategoryService.updateNoteCategory(id, noteCategoryRequestDto);

        return ResponseEntity.ok(noteCategoryDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNoteCategory(@PathVariable UUID id) {
        noteCategoryService.deleteNoteCategory(id);

        return ResponseEntity.noContent().build();
    }
}
