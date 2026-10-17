package dev.kafetzi.jobmappe.tracking.note;

import dev.kafetzi.jobmappe.common.pagination.PagedResponse;
import dev.kafetzi.jobmappe.tracking.note.dto.NoteCompactDto;
import dev.kafetzi.jobmappe.tracking.note.dto.NoteDetailDto;
import dev.kafetzi.jobmappe.tracking.note.dto.NoteRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notes")
public class NoteController {
    private final NoteService noteService;

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

    @GetMapping("/{id}")
    public ResponseEntity<NoteDetailDto> getNote(@PathVariable UUID id) {
        NoteDetailDto note = noteService.getNote(id);

        return ResponseEntity.ok(note);
    }

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

    @PutMapping("/{id}")
    public ResponseEntity<NoteDetailDto> updateNote(
            @PathVariable UUID id,
            @Valid @RequestBody NoteRequestDto noteRequestDto
    ) {
        NoteDetailDto note = noteService.updateNote(id, noteRequestDto);

        return ResponseEntity.ok(note);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable UUID id) {
        noteService.deleteNote(id);

        return ResponseEntity.noContent().build();
    }
}
