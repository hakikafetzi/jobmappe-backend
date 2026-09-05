package dev.kafetzi.jobmappe.tracking.mapper;

import dev.kafetzi.jobmappe.tracking.model.Application;
import dev.kafetzi.jobmappe.tracking.model.dto.ApplicationCompactDto;
import dev.kafetzi.jobmappe.tracking.model.dto.ApplicationResponseDto;
import dev.kafetzi.jobmappe.tracking.model.dto.ApplicationStatusDto;

public interface ApplicationMapper {

    ApplicationCompactDto toCompactDto(Application application, Integer daysSinceApplication);
    ApplicationStatusDto toStatusDto(Application application);
    ApplicationResponseDto toResponseDto(Application application);
}
