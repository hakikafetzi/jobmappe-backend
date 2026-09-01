package dev.kafetzi.jobmappe.posting.service;

import dev.kafetzi.jobmappe.posting.model.dto.JobPostingRequestDto;
import dev.kafetzi.jobmappe.posting.model.dto.JobPostingResponseDto;
import dev.kafetzi.jobmappe.posting.model.dto.JobPostingWithoutCompanyResponseDto;

import java.util.List;
import java.util.UUID;

public interface JobPostingService {
    List<JobPostingResponseDto> getAllJobPostings();
    JobPostingResponseDto createJobPosting(UUID companyId, JobPostingRequestDto jobPostingRequestDto);
    JobPostingResponseDto getJobPosting(UUID postingId);
    List<JobPostingWithoutCompanyResponseDto> getAllJobPostingsByCompanyId(UUID companyId);
    JobPostingResponseDto updateJobPosting(UUID id, JobPostingRequestDto jobPostingRequestDto);
}
