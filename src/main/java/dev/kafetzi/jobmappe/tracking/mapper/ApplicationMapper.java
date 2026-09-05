package dev.kafetzi.jobmappe.tracking.mapper;

import dev.kafetzi.jobmappe.tracking.model.Application;
import dev.kafetzi.jobmappe.tracking.model.dto.ApplicationCompactDto;
import dev.kafetzi.jobmappe.tracking.model.dto.ApplicationDetailDto;
import dev.kafetzi.jobmappe.tracking.model.dto.ApplicationResponseDto;

public interface ApplicationMapper {

    ApplicationCompactDto toCompactDto(Application application, Integer daysSinceApplication);
    ApplicationResponseDto toResponseDto(Application application);
    ApplicationDetailDto toDetailDto(Application application);
}
