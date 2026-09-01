package dev.kafetzi.jobmappe.posting.controller;

import dev.kafetzi.jobmappe.posting.model.dto.JobPostingRequestDto;
import dev.kafetzi.jobmappe.posting.model.dto.JobPostingResponseDto;
import dev.kafetzi.jobmappe.posting.model.dto.JobPostingWithoutCompanyResponseDto;
import dev.kafetzi.jobmappe.posting.service.JobPostingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class JobPostingController {
    private final JobPostingService jobPostingService;

    @GetMapping("/api/job-postings")
    public ResponseEntity<List<JobPostingResponseDto>> getAllJobPostings() {
        List<JobPostingResponseDto> jobPostingResponseDtos = jobPostingService.getAllJobPostings();

        return ResponseEntity.ok(jobPostingResponseDtos);
    }

    @GetMapping("/api/job-postings/{id}")
    public ResponseEntity<JobPostingResponseDto> getJobPosting(@PathVariable UUID id) {
        JobPostingResponseDto jobPosting = jobPostingService.getJobPosting(id);

        return ResponseEntity.ok(jobPosting);
    }

    @PutMapping("/api/job-postings/{id}")
    public ResponseEntity<JobPostingResponseDto> updateJobPosting(
            @PathVariable UUID id,
            @Valid @RequestBody JobPostingRequestDto jobPostingRequestDto
            ) {
        JobPostingResponseDto jobPosting = jobPostingService.updateJobPosting(id, jobPostingRequestDto);

        return ResponseEntity.ok(jobPosting);
    }

    @PostMapping("/api/companies/{companyId}/job-postings")
    public ResponseEntity<JobPostingResponseDto> createJobPosting(
            @PathVariable UUID companyId,
            @Valid @RequestBody JobPostingRequestDto jobPostingRequestDto
    ) {
        JobPostingResponseDto jobPosting = jobPostingService.createJobPosting(companyId, jobPostingRequestDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/job-postings/{id}")
                .buildAndExpand(jobPosting.id())
                .toUri();

        return ResponseEntity.created(location).body(jobPosting);
    }

    @GetMapping("/api/companies/{companyId}/job-postings")
    public ResponseEntity<List<JobPostingWithoutCompanyResponseDto>> getAllJobPostingsByCompany(
            @PathVariable UUID companyId
    ) {
        List<JobPostingWithoutCompanyResponseDto> jobPostings = jobPostingService.getAllJobPostingsByCompanyId(companyId);

        return ResponseEntity.ok(jobPostings);
    }
}
