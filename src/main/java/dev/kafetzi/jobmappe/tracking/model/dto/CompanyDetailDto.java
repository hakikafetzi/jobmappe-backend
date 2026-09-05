package dev.kafetzi.jobmappe.tracking.model.dto;

import java.util.UUID;

public record CompanyDetailDto(
        UUID id,
        String name,
        String website,
        String street,
        String postalCode,
        String city,
        long jobsCount
) {
}
