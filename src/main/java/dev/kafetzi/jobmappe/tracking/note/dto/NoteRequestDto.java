package dev.kafetzi.jobmappe.tracking.note.dto;

import dev.kafetzi.jobmappe.common.text.TextUtils;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record NoteRequestDto(
        UUID applicationId,

        UUID noteCategoryId,

        @NotBlank(message = "The title is required")
        @Size(max = 255, message = "The title must not exceed 255 characters")
        String title,

        String content
) {
        public NoteRequestDto {
                title = TextUtils.normalize(title);
        }
}
