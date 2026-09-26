package dev.kafetzi.jobmappe.tracking.note.dto;

import jakarta.validation.constraints.Size;

import java.util.UUID;

public record NoteRequestDto(
        UUID noteCategoryId,

        @Size(max = 255, message = "The title must not exceed 255 characters")
        String title,

        String content
) {
}
