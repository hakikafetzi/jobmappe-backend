package dev.kafetzi.jobmappe.posting.model.dto;

import java.util.UUID;

public record CompactCompanyDto(
        UUID id,
        String name,
        String website
) {
}
