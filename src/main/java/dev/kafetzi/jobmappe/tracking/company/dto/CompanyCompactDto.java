package dev.kafetzi.jobmappe.tracking.company.dto;

import java.util.UUID;

public record CompanyCompactDto(
        UUID id,
        String name,
        long jobsCount
) {
}
