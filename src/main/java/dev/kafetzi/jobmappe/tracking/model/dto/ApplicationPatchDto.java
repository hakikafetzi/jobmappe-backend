package dev.kafetzi.jobmappe.tracking.model.dto;

import dev.kafetzi.jobmappe.tracking.model.ApplicationPriority;
import dev.kafetzi.jobmappe.tracking.model.ApplicationStatus;

public record ApplicationPatchDto(
        ApplicationStatus status,
        ApplicationPriority priority,
        String appliedVia,
        String portalUrl
) {
}
