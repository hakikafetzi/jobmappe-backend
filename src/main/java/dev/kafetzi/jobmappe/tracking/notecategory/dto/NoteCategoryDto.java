package dev.kafetzi.jobmappe.tracking.notecategory.dto;

import java.util.UUID;

public record NoteCategoryDto(
        UUID id,
        String name
) {
}
