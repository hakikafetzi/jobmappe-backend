package dev.kafetzi.jobmappe.tracking.jobposting;

import dev.kafetzi.jobmappe.tracking.jobposting.dto.JobPostingCompactDto;
import dev.kafetzi.jobmappe.tracking.jobposting.dto.JobPostingDetailDto;
import dev.kafetzi.jobmappe.tracking.jobposting.dto.JobPostingRequestDto;
import dev.kafetzi.jobmappe.tracking.jobposting.dto.JobPostingResponseDto;

import java.util.List;
import java.util.UUID;

public interface JobPostingService {
    List<JobPostingCompactDto> getAllJobPostings();
    JobPostingDetailDto getJobPosting(UUID postingId);
    JobPostingResponseDto createJobPosting(UUID companyId, JobPostingRequestDto jobPostingRequestDto);
    JobPostingResponseDto updateJobPosting(UUID id, JobPostingRequestDto jobPostingRequestDto);
    List<JobPostingCompactDto> getAllJobPostingsByCompanyId(UUID companyId);
}
