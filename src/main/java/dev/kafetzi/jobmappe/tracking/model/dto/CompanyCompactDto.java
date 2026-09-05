package dev.kafetzi.jobmappe.tracking.model.dto;

import java.util.UUID;

public record CompanyCompactDto(
        UUID id,
        String name,
        long jobsCount
) {
}
