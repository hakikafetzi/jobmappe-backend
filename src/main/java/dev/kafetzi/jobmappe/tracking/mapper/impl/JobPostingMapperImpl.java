package dev.kafetzi.jobmappe.tracking.mapper.impl;

import dev.kafetzi.jobmappe.tracking.mapper.CompanyMapper;
import dev.kafetzi.jobmappe.tracking.mapper.JobPostingMapper;
import dev.kafetzi.jobmappe.tracking.model.JobPosting;
import dev.kafetzi.jobmappe.tracking.model.dto.JobPostingCompactDto;
import dev.kafetzi.jobmappe.tracking.model.dto.JobPostingDetailDto;
import dev.kafetzi.jobmappe.tracking.model.dto.JobPostingRequestDto;
import dev.kafetzi.jobmappe.tracking.model.dto.JobPostingResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
    public JobPostingDetailDto toDetailDto(JobPosting jobPosting) {
        return new JobPostingDetailDto(
                jobPosting.getId(),
                companyMapper.toDto(jobPosting.getCompany()),
                jobPosting.getTitle(),
                jobPosting.getDescription(),
                jobPosting.getLocation(),
                jobPosting.getUrl(),
                jobPosting.getStatus(),
                jobPosting.getPublishedAt(),
                jobPosting.getCreatedAt()
        );
    }

    @Override
    public JobPostingCompactDto toCompactDto(JobPosting jobPosting) {
        return new JobPostingCompactDto(
                jobPosting.getId(),
                jobPosting.getTitle(),
                jobPosting.getCompany().getName(),
                jobPosting.getLocation(),
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
