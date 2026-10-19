package dev.kafetzi.jobmappe.tracking.jobposting.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record JobPostingCompactDto(
        UUID id,
        String title,
        String companyName,
        String location,
        boolean hasApplication,
        LocalDateTime createdAt
) {
}
