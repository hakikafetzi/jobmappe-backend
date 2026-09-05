package dev.kafetzi.jobmappe.tracking.mapper.impl;

import dev.kafetzi.jobmappe.tracking.mapper.ApplicationMapper;
import dev.kafetzi.jobmappe.tracking.mapper.CompanyMapper;
import dev.kafetzi.jobmappe.tracking.mapper.JobPostingMapper;
import dev.kafetzi.jobmappe.tracking.model.Application;
import dev.kafetzi.jobmappe.tracking.model.dto.ApplicationCompactDto;
import dev.kafetzi.jobmappe.tracking.model.dto.ApplicationDetailDto;
import dev.kafetzi.jobmappe.tracking.model.dto.ApplicationResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplicationMapperImpl implements ApplicationMapper {
    private final JobPostingMapper jobPostingMapper;
    private final CompanyMapper companyMapper;

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

    @Override
    public ApplicationDetailDto toDetailDto(Application application) {
        return new ApplicationDetailDto(
                application.getId(),
                jobPostingMapper.toDto(application.getJobPosting()),
                companyMapper.toDto(application.getJobPosting().getCompany()),
                application.getStatus(),
                application.getPriority(),
                application.getAppliedAt(),
                application.getAppliedVia(),
                application.getPortalUrl(),
                application.getClosedAt()
        );
    }
}
