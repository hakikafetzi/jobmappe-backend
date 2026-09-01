package dev.kafetzi.jobmappe.posting.model.dto;

import java.util.UUID;

public record CompanyResponseDto(
        UUID id,
        String name,
        String website,
        long jobsCount
) {
}
