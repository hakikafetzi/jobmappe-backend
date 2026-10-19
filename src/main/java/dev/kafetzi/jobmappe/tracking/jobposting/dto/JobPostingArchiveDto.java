package dev.kafetzi.jobmappe.tracking.jobposting.dto;

import jakarta.validation.constraints.NotNull;

public record JobPostingArchiveDto(
        @NotNull(message = "The archived flag is required")
        Boolean archived
) {
}
