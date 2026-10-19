package dev.kafetzi.jobmappe.tracking.application;

import dev.kafetzi.jobmappe.common.pagination.PagedResponse;
import dev.kafetzi.jobmappe.tracking.application.dto.ApplicationCompactDto;
import dev.kafetzi.jobmappe.tracking.application.dto.ApplicationDetailDto;
import dev.kafetzi.jobmappe.tracking.application.dto.ApplicationPatchDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ApplicationController {
    private final ApplicationService applicationService;

    @GetMapping("/api/applications")
    public ResponseEntity<PagedResponse<ApplicationCompactDto>> getAllApplications(
            @RequestParam(name = "q", defaultValue = "") String q,
            @RequestParam(name = "status", required = false) ApplicationStatus status,
            @RequestParam(name = "priority", required = false) ApplicationPriority priority,
            @SortDefault(
                    sort = {"createdAt", "id"},
                    direction = Sort.Direction.DESC
            )
            Pageable pageable
    ) {
        Page<ApplicationCompactDto> applications = applicationService.getAllApplications(q, status, priority, pageable);

        return ResponseEntity.ok(PagedResponse.from(applications));
    }

    @GetMapping("/api/applications/{id}")
    public ResponseEntity<ApplicationDetailDto> getApplication(@PathVariable UUID id) {
        ApplicationDetailDto application = applicationService.getApplication(id);

        return ResponseEntity.ok(application);
    }

    @PatchMapping("/api/applications/{id}")
    public ResponseEntity<ApplicationDetailDto> updateApplication(
            @PathVariable UUID id,
            @Valid @RequestBody ApplicationPatchDto applicationPatchDto
    ) {
        ApplicationDetailDto application = applicationService.updateApplication(id, applicationPatchDto);

        return ResponseEntity.ok(application);
    }

    @PostMapping("/api/job-postings/{jobPostingId}/applications")
    public ResponseEntity<ApplicationDetailDto> startApplication(@PathVariable UUID jobPostingId) {
        ApplicationDetailDto application = applicationService.startApplication(jobPostingId);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/applications/{id}")
                .buildAndExpand(application.id())
                .toUri();

        return ResponseEntity.created(location).body(application);
    }

}
