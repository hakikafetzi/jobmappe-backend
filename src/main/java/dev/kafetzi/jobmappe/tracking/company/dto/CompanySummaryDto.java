package dev.kafetzi.jobmappe.tracking.company.dto;

import java.util.UUID;

public record CompanySummaryDto(
        UUID id,
        String name,
        String website,
        String street,
        String postalCode,
        String city
) {
}
