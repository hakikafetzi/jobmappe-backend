package dev.kafetzi.jobmappe.tracking.model.dto;

import java.util.UUID;

public record NoteRequestDto(
        UUID noteCategoryId,
        String title,
        String content
) {
}
