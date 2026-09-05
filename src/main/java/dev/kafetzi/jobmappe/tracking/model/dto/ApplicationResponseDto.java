package dev.kafetzi.jobmappe.tracking.model.dto;

import dev.kafetzi.jobmappe.tracking.model.ApplicationPriority;
import dev.kafetzi.jobmappe.tracking.model.ApplicationStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record ApplicationResponseDto(
        UUID id,
        ApplicationStatus status,
        ApplicationPriority priority,
        LocalDate appliedAt,
        String appliedVia,
        String portalUrl,
        LocalDate closedAt,
        LocalDateTime createdAt
) {
}
