package dev.kafetzi.jobmappe.tracking.controller;

import dev.kafetzi.jobmappe.tracking.model.dto.ApplicationCompactDto;
import dev.kafetzi.jobmappe.tracking.model.dto.ApplicationDetailDto;
import dev.kafetzi.jobmappe.tracking.model.dto.ApplicationResponseDto;
import dev.kafetzi.jobmappe.tracking.model.dto.ApplicationStatusDto;
import dev.kafetzi.jobmappe.tracking.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @GetMapping("/api/applications")
    public ResponseEntity<List<ApplicationCompactDto>> getAllApplications() {
        List<ApplicationCompactDto> applications = applicationService.getAllApplications();

        return ResponseEntity.ok(applications);
    }

    @GetMapping("api/applications/{id}")
    public ResponseEntity<ApplicationDetailDto> getApplication(@PathVariable UUID id) {

        return null;
    }

    @GetMapping("/api/job-postings/{jobPostingId}/applications")
    public ResponseEntity<ApplicationStatusDto> getApplicationByJobPostingId(@PathVariable UUID jobPostingId) {
        ApplicationStatusDto applicationStatusDto = applicationService.getApplicationByJobPostingId(jobPostingId);

        return ResponseEntity.ok(applicationStatusDto);
    }

    @PostMapping("/api/job-postings/{jobPostingId}/applications")
    public ResponseEntity<ApplicationResponseDto> createApplication(@PathVariable UUID jobPostingId) {
        ApplicationResponseDto application = applicationService.createApplication(jobPostingId);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/applications/{id}")
                .buildAndExpand(application.id())
                .toUri();

        return ResponseEntity.created(location).body(application);
    }

}
