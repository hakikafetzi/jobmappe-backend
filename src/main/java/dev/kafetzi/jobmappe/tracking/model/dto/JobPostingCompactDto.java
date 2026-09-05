package dev.kafetzi.jobmappe.tracking.model.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record JobPostingCompactDto(
        UUID id,
        String title,
        String companyName,
        String location,
        LocalDateTime createdAt
) {
}
