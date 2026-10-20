package dev.kafetzi.jobmappe.tracking.jobposting;

import dev.kafetzi.jobmappe.exception.ConflictException;
import dev.kafetzi.jobmappe.tracking.company.Company;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JobPostingTest {
    private JobPosting jobPosting;

    @BeforeEach
    void setUp() {
        Company company = new Company();
        jobPosting = new JobPosting(company);
    }

    @Test
    void archive_setsArchivedAt() {
        jobPosting.archive();

        assertThat(jobPosting.getArchivedAt()).isEqualTo(LocalDate.now());
    }

    @Test
    void archive_whenAlreadyArchived_throwsConflict() {
        jobPosting.archive();
        assertThat(jobPosting.getArchivedAt()).isNotNull();
        assertThatThrownBy(() -> jobPosting.archive()).isInstanceOf(ConflictException.class);
        assertThat(jobPosting.getArchivedAt()).isNotNull();
    }

    @Test
    void restore_clearsArchivedAt() {
        jobPosting.archive();
        assertThat(jobPosting.getArchivedAt()).isNotNull();

        jobPosting.restore();
        assertThat(jobPosting.getArchivedAt()).isNull();
    }

    @Test
    void restore_whenNotArchived_throwsConflict() {
        assertThat(jobPosting.getArchivedAt()).isNull();
        assertThatThrownBy(() -> jobPosting.restore()).isInstanceOf(ConflictException.class);
    }
}