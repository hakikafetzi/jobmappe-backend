package dev.kafetzi.jobmappe.tracking.application.dto;

import dev.kafetzi.jobmappe.tracking.application.ApplicationPriority;
import dev.kafetzi.jobmappe.tracking.application.ApplicationStatus;

public record ApplicationPatchDto(
        ApplicationStatus status,
        ApplicationPriority priority,
        String appliedVia,
        String portalUrl
) {
}
