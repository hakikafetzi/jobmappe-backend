package dev.kafetzi.jobmappe.tracking.note.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record NoteDetailDto(
        UUID id,
        String note_category,
        String title,
        String content,
        LocalDateTime created_at,
        LocalDateTime updated_at
) {
}
