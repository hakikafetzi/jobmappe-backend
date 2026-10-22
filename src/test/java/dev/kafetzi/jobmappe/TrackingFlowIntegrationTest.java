package dev.kafetzi.jobmappe;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
class TrackingFlowIntegrationTest {
    @Autowired MockMvcTester mvc;

    private String idOf(MvcTestResult result) {
        String body = new String(result.getResponse().getContentAsByteArray(), StandardCharsets.UTF_8);
        return JsonPath.read(body, "$.id");
    }

    @Test
    void trackingFlow_createCompanyPostingApplicationAndChangeStatus() {
        MvcTestResult companyResult = mvc.post().uri("/api/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"name": "Flow Test %s"}
                        """.formatted(UUID.randomUUID()))
                .exchange();
        assertThat(companyResult).hasStatus(HttpStatus.CREATED);
        String companyId = idOf(companyResult);

        MvcTestResult postingResult = mvc.post().uri("/api/companies/" + companyId + "/job-postings")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"title": "Backend Developer", "url": "https://example.com/job", "status": "ACTIVE"}
                        """)
                .exchange();
        assertThat(postingResult).hasStatus(HttpStatus.CREATED);
        String postingId = idOf(postingResult);

        MvcTestResult applicationResult = mvc.post().uri("/api/job-postings/" + postingId + "/applications")
                .exchange();
        assertThat(applicationResult).hasStatus(HttpStatus.CREATED);
        assertThat(applicationResult).bodyJson().extractingPath("$.status").isEqualTo("IN_PROGRESS");
        String applicationId = idOf(applicationResult);

        assertThat(mvc.patch().uri("/api/applications/" + applicationId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"status": "APPLIED"}
                        """))
                .hasStatusOk();

        assertThat(mvc.get().uri("/api/applications/" + applicationId))
                .hasStatusOk()
                .bodyJson().extractingPath("$.status").isEqualTo("APPLIED");
    }
}