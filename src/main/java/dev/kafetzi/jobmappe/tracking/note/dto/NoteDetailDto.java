package dev.kafetzi.jobmappe.tracking.note.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record NoteDetailDto(
        UUID id,
        String noteCategory,
        String title,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
