package dev.kafetzi.jobmappe.tracking.application;

import dev.kafetzi.jobmappe.exception.ConflictException;
import dev.kafetzi.jobmappe.tracking.company.Company;
import dev.kafetzi.jobmappe.tracking.jobposting.JobPosting;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ApplicationTest {
    private Application application;

    @BeforeEach
    void setUp() {
        Company company = new Company();
        JobPosting jobPosting = new JobPosting(company);
        application = new Application(jobPosting);
    }

    @Test
    void changeStatus_toApplied_setsAppliedAt() {
        application.changeStatus(ApplicationStatus.APPLIED);

        assertThat(application.getAppliedAt()).isEqualTo(LocalDate.now());
    }

    @Test
    void changeStatus_toInProgress_clearsApplicationDetails() {
        application.changeStatus(ApplicationStatus.APPLIED);
        application.setAppliedVia(ApplicationChannel.ONLINE);
        application.setPortalUrl("https://test.com");

        application.changeStatus(ApplicationStatus.IN_PROGRESS);

        assertThat(application.getAppliedAt()).isNull();
        assertThat(application.getAppliedVia()).isNull();
        assertThat(application.getPortalUrl()).isNull();
    }

    @Test
    void changeStatus_toFinalStatus_setsClosedAt() {
        application.changeStatus(ApplicationStatus.WITHDRAWN);

        assertThat(application.getClosedAt()).isEqualTo(LocalDate.now());
    }

    @Test
    void changeStatus_reopenApplication_clearsClosedAt() {
        application.changeStatus(ApplicationStatus.WITHDRAWN);
        assertThat(application.getClosedAt()).isNotNull();

        application.changeStatus(ApplicationStatus.IN_PROGRESS);
        assertThat(application.getClosedAt()).isNull();
    }

    @Test
    void changeStatus_notAllowedStatusTransition_throwsConflict() {
        assertThatThrownBy(() -> application.changeStatus(ApplicationStatus.OFFER_REJECTED))
                .isInstanceOf(ConflictException.class);
        assertThat(application.getStatus()).isEqualTo(ApplicationStatus.IN_PROGRESS);
    }
}