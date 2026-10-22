package dev.kafetzi.jobmappe.tracking.jobposting;

import dev.kafetzi.jobmappe.tracking.jobposting.dto.JobPostingArchiveDto;
import dev.kafetzi.jobmappe.tracking.jobposting.dto.JobPostingCompactDto;
import dev.kafetzi.jobmappe.tracking.jobposting.dto.JobPostingDetailDto;
import dev.kafetzi.jobmappe.tracking.jobposting.dto.JobPostingRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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

@WebMvcTest(JobPostingController.class)
class JobPostingControllerTest {
    private static final String VALID_BODY = """
            {"title": "Backend Developer", "url": "https://example.com/job", "status": "ACTIVE"}
            """;

    @Autowired MockMvcTester mvc;
    @MockitoBean JobPostingService jobPostingService;

    private JobPostingDetailDto jobPostingDetailDto(UUID id, String title) {
        return new JobPostingDetailDto(
                id, null, title, null, null, "https://example.com/job",
                JobPostingStatus.ACTIVE, null, null, LocalDateTime.now(), null
        );
    }

    private JobPostingCompactDto jobPostingCompactDto(String title) {
        return new JobPostingCompactDto(
                UUID.randomUUID(), title, "Acme", null, false, LocalDateTime.now()
        );
    }

    @Test
    void getAllJobPostings_returnsOkAndPassesParametersToService() {
        when(jobPostingService.getAllJobPostings(any(), any(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(jobPostingCompactDto("Backend Developer")), PageRequest.of(0, 20), 1));

        assertThat(mvc.get().uri("/api/job-postings?q=java&filter=OPEN"))
                .hasStatusOk()
                .bodyJson().extractingPath("$.content[0].title").isEqualTo("Backend Developer");

        verify(jobPostingService).getAllJobPostings(eq("java"), eq(JobPostingFilter.OPEN), any(Pageable.class));
    }

    @Test
    void getJobPosting_returnsOk() {
        UUID jobPostingId = UUID.randomUUID();
        when(jobPostingService.getJobPosting(jobPostingId))
                .thenReturn(jobPostingDetailDto(jobPostingId, "Backend Developer"));

        assertThat(mvc.get().uri("/api/job-postings/" + jobPostingId))
                .hasStatusOk()
                .bodyJson().extractingPath("$.title").isEqualTo("Backend Developer");
    }

    @Test
    void createJobPosting_returnsCreatedWithLocation() {
        UUID companyId = UUID.randomUUID();
        JobPostingDetailDto jobPostingDetailDto = jobPostingDetailDto(UUID.randomUUID(), "Backend Developer");
        when(jobPostingService.createJobPosting(eq(companyId), any(JobPostingRequestDto.class)))
                .thenReturn(jobPostingDetailDto);

        assertThat(mvc.post().uri("/api/companies/" + companyId + "/job-postings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(VALID_BODY))
                .hasStatus(HttpStatus.CREATED)
                .hasHeader("Location", "http://localhost/api/job-postings/" + jobPostingDetailDto.id())
                .bodyJson().extractingPath("$.title").isEqualTo("Backend Developer");
    }

    @Test
    void createJobPosting_withBlankTitle_returnsBadRequest() {
        assertThat(mvc.post().uri("/api/companies/" + UUID.randomUUID() + "/job-postings")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"title": "", "url": "https://example.com/job", "status": "ACTIVE"}
                        """))
                .hasStatus(HttpStatus.BAD_REQUEST)
                .bodyJson().extractingPath("$.errors.title").isNotNull();
    }

    @Test
    void updateJobPosting_returnsOk() {
        UUID jobPostingId = UUID.randomUUID();
        when(jobPostingService.updateJobPosting(eq(jobPostingId), any(JobPostingRequestDto.class)))
                .thenReturn(jobPostingDetailDto(jobPostingId, "Backend Developer"));

        assertThat(mvc.put().uri("/api/job-postings/" + jobPostingId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(VALID_BODY))
                .hasStatusOk()
                .bodyJson().extractingPath("$.title").isEqualTo("Backend Developer");
    }

    @Test
    void getAllJobPostingsByCompany_returnsOk() {
        UUID companyId = UUID.randomUUID();
        when(jobPostingService.getAllJobPostingsByCompanyId(eq(companyId), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(jobPostingCompactDto("Backend Developer")), PageRequest.of(0, 20), 1));

        assertThat(mvc.get().uri("/api/companies/" + companyId + "/job-postings"))
                .hasStatusOk()
                .bodyJson().extractingPath("$.content[0].title").isEqualTo("Backend Developer");
    }

    @Test
    void setArchived_returnsOk() {
        UUID jobPostingId = UUID.randomUUID();
        when(jobPostingService.setArchived(eq(jobPostingId), any(JobPostingArchiveDto.class)))
                .thenReturn(jobPostingDetailDto(jobPostingId, "Backend Developer"));

        assertThat(mvc.patch().uri("/api/job-postings/" + jobPostingId + "/archive")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"archived": true}
                        """))
                .hasStatusOk()
                .bodyJson().extractingPath("$.title").isEqualTo("Backend Developer");
    }
}