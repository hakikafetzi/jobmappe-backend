package dev.kafetzi.jobmappe.tracking.jobposting.dto;

import jakarta.validation.constraints.NotNull;

public record JobPostingArchiveDto(
        @NotNull(message = "The archived status is required")
        Boolean archived
) {
}
