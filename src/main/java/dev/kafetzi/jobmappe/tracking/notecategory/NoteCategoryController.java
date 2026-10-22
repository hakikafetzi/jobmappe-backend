package dev.kafetzi.jobmappe.tracking.notecategory;

import dev.kafetzi.jobmappe.tracking.notecategory.dto.NoteCategoryDto;
import dev.kafetzi.jobmappe.tracking.notecategory.dto.NoteCategoryRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Tag(name = "Note categories", description = "Categories for grouping notes")
@RestController
@RequestMapping("/api/note-categories")
@RequiredArgsConstructor
public class NoteCategoryController {
    private final NoteCategoryService noteCategoryService;

    @Operation(summary = "List note categories", description = "Returns all categories, sorted by name.")
    @ApiResponse(responseCode = "200", description = "All note categories")
    @GetMapping
    public ResponseEntity<List<NoteCategoryDto>> getAllNoteCategories() {
        List<NoteCategoryDto>  noteCategories = noteCategoryService.getAllNoteCategories();

        return ResponseEntity.ok(noteCategories);
    }

    @Operation(summary = "Get a note category")
    @ApiResponse(responseCode = "200", description = "Note category found")
    @ApiResponse(responseCode = "404", description = "Note category not found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @GetMapping("/{id}")
    public ResponseEntity<NoteCategoryDto> getNoteCategory(@PathVariable UUID id) {
        NoteCategoryDto noteCategory = noteCategoryService.getNoteCategoryById(id);

        return ResponseEntity.ok(noteCategory);
    }

    @Operation(summary = "Create a note category")
    @ApiResponse(responseCode = "201", description = "Note category created")
    @ApiResponse(responseCode = "400", description = "Invalid request body; field errors are listed in 'errors'",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "409", description = "A category with this name already exists",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @PostMapping
    public ResponseEntity<NoteCategoryDto> createNoteCategory(
            @Valid @RequestBody NoteCategoryRequestDto noteCategoryRequestDto
    ) {
        NoteCategoryDto noteCategoryDto = noteCategoryService.createNoteCategory(noteCategoryRequestDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/note-categories/{id}")
                .buildAndExpand(noteCategoryDto.id())
                .toUri();

        return ResponseEntity.created(location).body(noteCategoryDto);
    }

    @Operation(summary = "Rename a note category")
    @ApiResponse(responseCode = "200", description = "Note category updated")
    @ApiResponse(responseCode = "400", description = "Invalid request body; field errors are listed in 'errors'",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "Note category not found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "409", description = "Another category already uses this name",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @PutMapping("/{id}")
    public ResponseEntity<NoteCategoryDto> updateNoteCategory(
            @PathVariable UUID id,
            @Valid @RequestBody NoteCategoryRequestDto noteCategoryRequestDto
    ) {
        NoteCategoryDto noteCategoryDto = noteCategoryService.updateNoteCategory(id, noteCategoryRequestDto);

        return ResponseEntity.ok(noteCategoryDto);
    }

    @Operation(summary = "Delete a note category",
            description = "Notes in this category are kept and lose their category.")
    @ApiResponse(responseCode = "204", description = "Note category deleted")
    @ApiResponse(responseCode = "404", description = "Note category not found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNoteCategory(@PathVariable UUID id) {
        noteCategoryService.deleteNoteCategory(id);

        return ResponseEntity.noContent().build();
    }
}
