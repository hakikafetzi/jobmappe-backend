package dev.kafetzi.jobmappe.tracking.note;

import dev.kafetzi.jobmappe.tracking.note.dto.NoteCompactDto;
import dev.kafetzi.jobmappe.tracking.note.dto.NoteDetailDto;
import dev.kafetzi.jobmappe.tracking.note.dto.NoteRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @GetMapping("/api/applications/{applicationId}/notes")
    public ResponseEntity<List<NoteCompactDto>> getAllNotes(@PathVariable UUID applicationId) {
        List<NoteCompactDto> notes = noteService.getAllNotes(applicationId);

        return ResponseEntity.ok(notes);
    }

    @GetMapping("/api/notes/{id}")
    public ResponseEntity<NoteDetailDto> getNote(@PathVariable UUID id) {
        NoteDetailDto note = noteService.getNote(id);

        return ResponseEntity.ok(note);
    }

    @PostMapping("/api/applications/{applicationId}/notes")
    public ResponseEntity<NoteDetailDto> createNote(
            @PathVariable UUID applicationId,
            @Valid @RequestBody NoteRequestDto noteRequestDto
            ) {
        NoteDetailDto note = noteService.createNote(applicationId, noteRequestDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/notes/{id}")
                .buildAndExpand(note.id())
                .toUri();

        return ResponseEntity.created(location).body(note);
    }

    @PutMapping("/api/notes/{id}")
    public ResponseEntity<NoteDetailDto> updateNote(
            @PathVariable UUID id,
            @Valid @RequestBody NoteRequestDto noteRequestDto
    ) {
        NoteDetailDto note = noteService.updateNote(id, noteRequestDto);

        return ResponseEntity.ok(note);
    }
}
