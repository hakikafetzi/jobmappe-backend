package dev.kafetzi.jobmappe.tracking.model.dto;

import dev.kafetzi.jobmappe.tracking.model.ApplicationStatus;

import java.util.UUID;

public record ApplicationStatusDto(
        UUID id,
        ApplicationStatus status
) {
}
