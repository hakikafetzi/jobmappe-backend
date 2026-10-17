package dev.kafetzi.jobmappe.tracking.note.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record NoteCompactDto(
        UUID id,
        String noteCategory,
        String title,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        UUID applicationId,
        String jobTitle,
        String companyName
) {
}
