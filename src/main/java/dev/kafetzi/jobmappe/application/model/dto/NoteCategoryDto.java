package dev.kafetzi.jobmappe.application.model.dto;

import java.util.UUID;

public record NoteCategoryDto(
        UUID id,
        String name
) {
}
