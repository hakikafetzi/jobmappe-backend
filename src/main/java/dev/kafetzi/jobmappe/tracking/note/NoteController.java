package dev.kafetzi.jobmappe.tracking.note;

import dev.kafetzi.jobmappe.common.pagination.PagedResponse;
import dev.kafetzi.jobmappe.tracking.note.dto.NoteCompactDto;
import dev.kafetzi.jobmappe.tracking.note.dto.NoteDetailDto;
import dev.kafetzi.jobmappe.tracking.note.dto.NoteRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@Tag(name = "Notes", description = "Notes, optionally linked to an application and a category")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notes")
public class NoteController {
    private final NoteService noteService;

    @Operation(summary = "List notes",
            description = "Returns a page of notes, optionally filtered by noteCategoryId and/or applicationId. "
                    + "q searches title and content and cannot be combined with a filter.")
    @ApiResponse(responseCode = "200", description = "Page of notes")
    @ApiResponse(responseCode = "400", description = "Invalid sort property, or q combined with a filter",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @GetMapping
    public ResponseEntity<PagedResponse<NoteCompactDto>> getAllNotes(
            @RequestParam(name = "q", defaultValue = "") String q,
            @RequestParam(name = "noteCategoryId", required = false) UUID noteCategoryId,
            @RequestParam(name = "applicationId", required = false) UUID applicationId,
            @SortDefault(
                    sort = {"createdAt", "id"},
                    direction = Sort.Direction.DESC
            )
            Pageable pageable
    ) {
        Page<NoteCompactDto> notes = noteService.getAllNotes(q, noteCategoryId, applicationId, pageable);

        return ResponseEntity.ok(PagedResponse.from(notes));
    }

    @Operation(summary = "Get a note")
    @ApiResponse(responseCode = "200", description = "Note found")
    @ApiResponse(responseCode = "404", description = "Note not found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @GetMapping("/{id}")
    public ResponseEntity<NoteDetailDto> getNote(@PathVariable UUID id) {
        NoteDetailDto note = noteService.getNote(id);

        return ResponseEntity.ok(note);
    }

    @Operation(summary = "Create a note")
    @ApiResponse(responseCode = "201", description = "Note created")
    @ApiResponse(responseCode = "400", description = "Invalid request body; field errors are listed in 'errors'",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "The given application or note category was not found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @PostMapping
    public ResponseEntity<NoteDetailDto> createNote(
            @Valid @RequestBody NoteRequestDto noteRequestDto
    ) {
        NoteDetailDto note = noteService.createNote(noteRequestDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/notes/{id}")
                .buildAndExpand(note.id())
                .toUri();

        return ResponseEntity.created(location).body(note);
    }

    @Operation(summary = "Update a note")
    @ApiResponse(responseCode = "200", description = "Note updated")
    @ApiResponse(responseCode = "400", description = "Invalid request body; field errors are listed in 'errors'",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "The note, or the given application or note category, was not found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @PutMapping("/{id}")
    public ResponseEntity<NoteDetailDto> updateNote(
            @PathVariable UUID id,
            @Valid @RequestBody NoteRequestDto noteRequestDto
    ) {
        NoteDetailDto note = noteService.updateNote(id, noteRequestDto);

        return ResponseEntity.ok(note);
    }

    @Operation(summary = "Delete a note")
    @ApiResponse(responseCode = "204", description = "Note deleted")
    @ApiResponse(responseCode = "404", description = "Note not found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable UUID id) {
        noteService.deleteNote(id);

        return ResponseEntity.noContent().build();
    }
}
