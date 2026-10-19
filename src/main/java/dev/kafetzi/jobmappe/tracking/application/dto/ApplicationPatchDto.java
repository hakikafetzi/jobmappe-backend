package dev.kafetzi.jobmappe.tracking.application.dto;

import dev.kafetzi.jobmappe.common.text.TextUtils;
import dev.kafetzi.jobmappe.tracking.application.ApplicationChannel;
import dev.kafetzi.jobmappe.tracking.application.ApplicationPriority;
import dev.kafetzi.jobmappe.tracking.application.ApplicationStatus;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ApplicationPatchDto(
        ApplicationStatus status,

        ApplicationPriority priority,

        ApplicationChannel appliedVia,

        @Pattern(
                regexp = "https?://\\S+",
                flags = Pattern.Flag.CASE_INSENSITIVE,
                message = "The portalUrl must start with http:// or https://"
        )
        @Size(max = 1000, message = "The portalUrl must not exceed 1000 characters")
        String portalUrl
) {
        public ApplicationPatchDto {
                portalUrl = TextUtils.normalize(portalUrl);
        }
}
