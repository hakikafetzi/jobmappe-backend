package dev.kafetzi.jobmappe.tracking.application.dto;

import dev.kafetzi.jobmappe.tracking.application.ApplicationPriority;
import dev.kafetzi.jobmappe.tracking.application.ApplicationStatus;

import java.time.LocalDate;
import java.util.UUID;

public record ApplicationCompactDto(
        UUID id,
        String jobTitle,
        String companyName,
        ApplicationStatus status,
        ApplicationPriority priority,
        LocalDate appliedAt,
        Integer daysSinceApplication
) {
}
