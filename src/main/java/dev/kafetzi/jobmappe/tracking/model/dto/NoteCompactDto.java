package dev.kafetzi.jobmappe.tracking.model.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record NoteCompactDto(
        UUID id,
        String note_category,
        String title,
        LocalDateTime created_at
) {
}
