package dev.kafetzi.jobmappe.tracking.application;

import dev.kafetzi.jobmappe.tracking.application.dto.ApplicationCompactDto;
import dev.kafetzi.jobmappe.tracking.application.dto.ApplicationDetailDto;
import dev.kafetzi.jobmappe.tracking.application.dto.ApplicationPatchDto;
import dev.kafetzi.jobmappe.tracking.application.dto.ApplicationResponseDto;
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
public class ApplicationController {

    private final ApplicationService applicationService;

    @GetMapping("/api/applications")
    public ResponseEntity<List<ApplicationCompactDto>> getAllApplications() {
        List<ApplicationCompactDto> applications = applicationService.getAllApplications();

        return ResponseEntity.ok(applications);
    }

    @GetMapping("/api/applications/{id}")
    public ResponseEntity<ApplicationDetailDto> getApplication(@PathVariable UUID id) {
        ApplicationDetailDto application = applicationService.getApplication(id);

        return ResponseEntity.ok(application);
    }

    @PatchMapping("/api/applications/{id}")
    public ResponseEntity<ApplicationResponseDto> updateApplication(
            @PathVariable UUID id,
            @Valid @RequestBody ApplicationPatchDto applicationPatchDto
    ) {
        ApplicationResponseDto application = applicationService.updateApplication(id, applicationPatchDto);

        return ResponseEntity.ok(application);
    }

    @PostMapping("/api/job-postings/{jobPostingId}/applications")
    public ResponseEntity<ApplicationResponseDto> startApplication(@PathVariable UUID jobPostingId) {
        ApplicationResponseDto application = applicationService.startApplication(jobPostingId);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/applications/{id}")
                .buildAndExpand(application.id())
                .toUri();

        return ResponseEntity.created(location).body(application);
    }

}
