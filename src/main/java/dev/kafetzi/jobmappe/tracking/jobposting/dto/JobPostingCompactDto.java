package dev.kafetzi.jobmappe.tracking.jobposting.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record JobPostingCompactDto(
        UUID id,
        String title,
        String companyName,
        String location,
        Boolean hasApplication,
        LocalDateTime createdAt,
        LocalDate archivedAt
) {
}
