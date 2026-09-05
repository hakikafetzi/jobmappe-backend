package dev.kafetzi.jobmappe.tracking.mapper.impl;

import dev.kafetzi.jobmappe.tracking.mapper.ApplicationMapper;
import dev.kafetzi.jobmappe.tracking.model.Application;
import dev.kafetzi.jobmappe.tracking.model.dto.ApplicationCompactDto;
import dev.kafetzi.jobmappe.tracking.model.dto.ApplicationResponseDto;
import org.springframework.stereotype.Component;

@Component
public class ApplicationMapperImpl implements ApplicationMapper {
    @Override
    public ApplicationCompactDto toCompactDto(Application application, Integer daysSinceApplication) {
        return new ApplicationCompactDto(
                application.getId(),
                application.getJobPosting().getTitle(),
                application.getJobPosting().getCompany().getName(),
                application.getStatus(),
                application.getPriority(),
                application.getAppliedAt(),
                daysSinceApplication
        );
    }

    @Override
    public ApplicationResponseDto toResponseDto(Application application) {
        return new ApplicationResponseDto(
                application.getId(),
                application.getStatus(),
                application.getPriority(),
                application.getAppliedAt(),
                application.getAppliedVia(),
                application.getPortalUrl(),
                application.getClosedAt(),
                application.getCreatedAt()
        );
    }
}
