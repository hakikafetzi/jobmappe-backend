package dev.kafetzi.jobmappe.posting.mapper.impl;

import dev.kafetzi.jobmappe.posting.mapper.CompanyMapper;
import dev.kafetzi.jobmappe.posting.mapper.JobPostingMapper;
import dev.kafetzi.jobmappe.posting.model.JobPosting;
import dev.kafetzi.jobmappe.posting.model.dto.JobPostingRequestDto;
import dev.kafetzi.jobmappe.posting.model.dto.JobPostingResponseDto;
import dev.kafetzi.jobmappe.posting.model.dto.JobPostingWithoutCompanyResponseDto;
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
                jobPosting.getPublishedAt(),
                companyMapper.toCompactDto(jobPosting.getCompany())
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
    public JobPostingWithoutCompanyResponseDto toDtoWithoutCompany(JobPosting jobPosting) {
        return new JobPostingWithoutCompanyResponseDto(
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
