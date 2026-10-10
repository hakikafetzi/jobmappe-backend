package dev.kafetzi.jobmappe.tracking.application.dto;

import dev.kafetzi.jobmappe.tracking.application.ApplicationChannel;
import dev.kafetzi.jobmappe.tracking.application.ApplicationPriority;
import dev.kafetzi.jobmappe.tracking.application.ApplicationStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record ApplicationResponseDto(
        UUID id,
        ApplicationStatus status,
        ApplicationPriority priority,
        LocalDate appliedAt,
        ApplicationChannel appliedVia,
        String portalUrl,
        LocalDate closedAt,
        LocalDateTime createdAt
) {
}
