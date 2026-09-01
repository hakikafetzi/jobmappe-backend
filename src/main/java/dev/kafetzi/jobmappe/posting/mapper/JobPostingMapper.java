package dev.kafetzi.jobmappe.posting.mapper;

import dev.kafetzi.jobmappe.posting.model.JobPosting;
import dev.kafetzi.jobmappe.posting.model.dto.JobPostingRequestDto;
import dev.kafetzi.jobmappe.posting.model.dto.JobPostingResponseDto;
import dev.kafetzi.jobmappe.posting.model.dto.JobPostingWithoutCompanyResponseDto;

public interface JobPostingMapper {
    JobPostingResponseDto toDto(JobPosting jobPosting);
    JobPosting toEntity(JobPostingRequestDto jobPostingRequestDto);
    JobPostingWithoutCompanyResponseDto toDtoWithoutCompany(JobPosting jobPosting);
    void updateEntityFromDto(JobPostingRequestDto jobPostingRequestDto, JobPosting jobPosting);
}
