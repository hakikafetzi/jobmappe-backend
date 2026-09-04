package dev.kafetzi.jobmappe.posting.model.dto;

import java.util.UUID;

public record CompanyCompactDto(
        UUID id,
        String name,
        long jobsCount
) {
}
