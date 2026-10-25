package dev.kafetzi.jobmappe;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
class ForwardedHeadersIntegrationTest {
    @Autowired MockMvcTester mvc;

    @Test
    void createCompany_usesForwardedHostInLocationHeader() {
        MvcTestResult result = mvc.post().uri("/api/companies")
                .header("X-Forwarded-Proto", "https")
                .header("X-Forwarded-Host", "jobmappe.example")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"name": "Forwarded Test %s"}
                        """.formatted(UUID.randomUUID()))
                .exchange();

        assertThat(result).hasStatus(HttpStatus.CREATED);
        assertThat(result.getResponse().getHeader("Location"))
                .startsWith("https://jobmappe.example/api/companies/");
    }
}