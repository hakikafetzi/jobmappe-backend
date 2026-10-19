package dev.kafetzi.jobmappe.tracking.jobposting.impl;

import dev.kafetzi.jobmappe.tracking.company.Company;
import dev.kafetzi.jobmappe.tracking.company.CompanyMapper;
import dev.kafetzi.jobmappe.tracking.jobposting.JobPostingMapper;
import dev.kafetzi.jobmappe.tracking.jobposting.JobPosting;
import dev.kafetzi.jobmappe.tracking.jobposting.dto.JobPostingCompactDto;
import dev.kafetzi.jobmappe.tracking.jobposting.dto.JobPostingDetailDto;
import dev.kafetzi.jobmappe.tracking.jobposting.dto.JobPostingRequestDto;
import dev.kafetzi.jobmappe.tracking.jobposting.dto.JobPostingSummaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JobPostingMapperImpl implements JobPostingMapper {
    private final CompanyMapper companyMapper;

    @Override
    public JobPostingSummaryDto toSummaryDto(JobPosting jobPosting) {
        return new JobPostingSummaryDto(
                jobPosting.getId(),
                jobPosting.getTitle(),
                jobPosting.getDescription(),
                jobPosting.getLocation(),
                jobPosting.getUrl(),
                jobPosting.getStatus(),
                jobPosting.getPublishedAt(),
                jobPosting.getArchivedAt()
        );
    }

    @Override
    public JobPosting toEntity(JobPostingRequestDto jobPostingRequestDto, Company company) {
        JobPosting jobPosting = new JobPosting(company);

        jobPosting.setTitle(jobPostingRequestDto.title());
        jobPosting.setDescription(jobPostingRequestDto.description());
        jobPosting.setLocation(jobPostingRequestDto.location());
        jobPosting.setUrl(jobPostingRequestDto.url());
        jobPosting.setStatus(jobPostingRequestDto.status());
        jobPosting.setPublishedAt(jobPostingRequestDto.publishedAt());

        return jobPosting;
    }

    @Override
    public JobPostingDetailDto toDetailDto(JobPosting jobPosting, UUID applicationId) {
        return new JobPostingDetailDto(
                jobPosting.getId(),
                companyMapper.toSummaryDto(jobPosting.getCompany()),
                jobPosting.getTitle(),
                jobPosting.getDescription(),
                jobPosting.getLocation(),
                jobPosting.getUrl(),
                jobPosting.getStatus(),
                applicationId,
                jobPosting.getPublishedAt(),
                jobPosting.getCreatedAt(),
                jobPosting.getArchivedAt()
        );
    }

    @Override
    public JobPostingCompactDto toCompactDto(JobPosting jobPosting, boolean hasApplication) {
        return new JobPostingCompactDto(
                jobPosting.getId(),
                jobPosting.getTitle(),
                jobPosting.getCompany().getName(),
                jobPosting.getLocation(),
                hasApplication,
                jobPosting.getCreatedAt()
        );
    }

    @Override
    public void updateEntityFromDto(JobPostingRequestDto jobPostingRequestDto, JobPosting jobPosting) {
        jobPosting.setTitle(jobPostingRequestDto.title());
        jobPosting.setDescription(jobPostingRequestDto.description());
        jobPosting.setLocation(jobPostingRequestDto.location());
        jobPosting.setUrl(jobPostingRequestDto.url());
        jobPosting.setStatus(jobPostingRequestDto.status());
        jobPosting.setPublishedAt(jobPostingRequestDto.publishedAt());
    }
}
