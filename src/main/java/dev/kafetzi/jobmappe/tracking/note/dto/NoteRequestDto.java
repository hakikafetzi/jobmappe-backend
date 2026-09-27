package dev.kafetzi.jobmappe.tracking.note.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record NoteRequestDto(
        UUID noteCategoryId,

        @NotBlank(message = "The title must not be blank")
        @Size(max = 255, message = "The title must not exceed 255 characters")
        String title,

        String content
) {
}
