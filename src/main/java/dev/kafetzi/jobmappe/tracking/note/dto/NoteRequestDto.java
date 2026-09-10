package dev.kafetzi.jobmappe.tracking.note.dto;

import java.util.UUID;

public record NoteRequestDto(
        UUID noteCategoryId,
        String title,
        String content
) {
}
