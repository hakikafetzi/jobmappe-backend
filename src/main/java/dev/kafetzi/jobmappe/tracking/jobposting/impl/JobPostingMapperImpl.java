package dev.kafetzi.jobmappe.tracking.jobposting.impl;

import dev.kafetzi.jobmappe.tracking.company.CompanyMapper;
import dev.kafetzi.jobmappe.tracking.jobposting.JobPostingMapper;
import dev.kafetzi.jobmappe.tracking.jobposting.JobPosting;
import dev.kafetzi.jobmappe.tracking.jobposting.dto.JobPostingCompactDto;
import dev.kafetzi.jobmappe.tracking.jobposting.dto.JobPostingDetailDto;
import dev.kafetzi.jobmappe.tracking.jobposting.dto.JobPostingRequestDto;
import dev.kafetzi.jobmappe.tracking.jobposting.dto.JobPostingResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JobPostingMapperImpl implements JobPostingMapper {

    private final CompanyMapper companyMapper;

    @Override
    public JobPostingResponseDto toDto(JobPosting jobPosting) {
        return new JobPostingResponseDto(
                jobPosting.getId(),
                jobPosting.getTitle(),
                jobPosting.getDescription(),
                jobPosting.getLocation(),
                jobPosting.getUrl(),
                jobPosting.getStatus(),
                jobPosting.getPublishedAt()
        );
    }

    @Override
    public JobPosting toEntity(JobPostingRequestDto jobPostingRequestDto) {
        JobPosting jobPosting = new JobPosting();

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
                companyMapper.toDto(jobPosting.getCompany()),
                jobPosting.getTitle(),
                jobPosting.getDescription(),
                jobPosting.getLocation(),
                jobPosting.getUrl(),
                jobPosting.getStatus(),
                applicationId != null,
                applicationId,
                jobPosting.getPublishedAt(),
                jobPosting.getCreatedAt()
        );
    }

    @Override
    public JobPostingCompactDto toCompactDto(JobPosting jobPosting, Boolean hasApplication) {
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
        if (jobPostingRequestDto == null || jobPosting == null) return;

        jobPosting.setTitle(jobPostingRequestDto.title());
        jobPosting.setDescription(jobPostingRequestDto.description());
        jobPosting.setLocation(jobPostingRequestDto.location());
        jobPosting.setUrl(jobPostingRequestDto.url());
        jobPosting.setStatus(jobPostingRequestDto.status());
        jobPosting.setPublishedAt(jobPostingRequestDto.publishedAt());
    }
}
