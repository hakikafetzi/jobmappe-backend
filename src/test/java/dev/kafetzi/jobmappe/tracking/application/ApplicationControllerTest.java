package dev.kafetzi.jobmappe.tracking.application;

import dev.kafetzi.jobmappe.tracking.application.dto.ApplicationDetailDto;
import dev.kafetzi.jobmappe.tracking.application.dto.ApplicationPatchDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(ApplicationController.class)
class ApplicationControllerTest {
    @Autowired MockMvcTester mvc;
    @MockitoBean ApplicationService applicationService;

    private ApplicationDetailDto applicationDetailDto(UUID id, ApplicationStatus status) {
        return new ApplicationDetailDto(
                id, null, null, status, ApplicationPriority.NONE,
                null, null, null, null, LocalDateTime.now()
        );
    }

    @Test
    void getAllApplications_withFilters_passesParametersToService() {
        when(applicationService.getAllApplications(any(), any(), any(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of()));

        assertThat(mvc.get().uri("/api/applications?q=foo&status=APPLIED&priority=HIGH"))
                .hasStatusOk();

        verify(applicationService).getAllApplications(
                eq("foo"), eq(ApplicationStatus.APPLIED), eq(ApplicationPriority.HIGH), any(Pageable.class)
        );
    }

    @Test
    void getAllApplications_withInvalidStatus_returnsBadRequest() {
        assertThat(mvc.get().uri("/api/applications?status=FOO"))
                .hasStatus(HttpStatus.BAD_REQUEST);
    }

    @Test
    void getApplication_returnsOk() {
        UUID applicationId = UUID.randomUUID();
        when(applicationService.getApplication(applicationId))
                .thenReturn(applicationDetailDto(applicationId, ApplicationStatus.APPLIED));

        assertThat(mvc.get().uri("/api/applications/" + applicationId))
                .hasStatusOk()
                .bodyJson().extractingPath("$.status").isEqualTo("APPLIED");
    }

    @Test
    void updateApplication_returnsOk() {
        UUID applicationId = UUID.randomUUID();
        when(applicationService.updateApplication(eq(applicationId), any(ApplicationPatchDto.class)))
                .thenReturn(applicationDetailDto(applicationId, ApplicationStatus.APPLIED));

        assertThat(mvc.patch().uri("/api/applications/" + applicationId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"status": "APPLIED"}
                        """))
                .hasStatusOk()
                .bodyJson().extractingPath("$.status").isEqualTo("APPLIED");
    }

    @Test
    void startApplication_returnsCreatedWithLocation() {
        UUID jobPostingId = UUID.randomUUID();
        ApplicationDetailDto applicationDetailDto =
                applicationDetailDto(UUID.randomUUID(), ApplicationStatus.IN_PROGRESS);
        when(applicationService.startApplication(jobPostingId)).thenReturn(applicationDetailDto);

        assertThat(mvc.post().uri("/api/job-postings/" + jobPostingId + "/applications"))
                .hasStatus(HttpStatus.CREATED)
                .hasHeader("Location", "http://localhost/api/applications/" + applicationDetailDto.id())
                .bodyJson().extractingPath("$.status").isEqualTo("IN_PROGRESS");
    }
}