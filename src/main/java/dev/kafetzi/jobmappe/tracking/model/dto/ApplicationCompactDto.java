package dev.kafetzi.jobmappe.tracking.model.dto;

import dev.kafetzi.jobmappe.tracking.model.ApplicationPriority;
import dev.kafetzi.jobmappe.tracking.model.ApplicationStatus;

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
