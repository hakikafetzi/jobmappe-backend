package dev.kafetzi.jobmappe.tracking.application.dto;

import dev.kafetzi.jobmappe.tracking.application.ApplicationChannel;
import dev.kafetzi.jobmappe.tracking.application.ApplicationPriority;
import dev.kafetzi.jobmappe.tracking.application.ApplicationStatus;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ApplicationPatchDto(
        ApplicationStatus status,

        ApplicationPriority priority,

        ApplicationChannel appliedVia,

        @Pattern(regexp = ".*\\S.*", message = "The portal url must not be blank or contain only whitespace")
        @Size(max = 1000, message = "The portal url must not exceed 1000 characters")
        String portalUrl
) {
}
