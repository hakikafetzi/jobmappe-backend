package dev.kafetzi.jobmappe.tracking.application.dto;

import dev.kafetzi.jobmappe.tracking.application.ApplicationPriority;
import dev.kafetzi.jobmappe.tracking.application.ApplicationStatus;
import jakarta.validation.constraints.Size;

public record ApplicationPatchDto(
        @Size(max = 50, message = "The status must not exceed 50 characters")
        ApplicationStatus status,

        @Size(max = 50, message = "The priority must not exceed 50 characters")
        ApplicationPriority priority,

        @Size(max = 255, message = "The applied via must not exceed 255 characters")
        String appliedVia,

        @Size(max = 1000, message = "The portal url must not exceed 1000 characters")
        String portalUrl
) {
}
