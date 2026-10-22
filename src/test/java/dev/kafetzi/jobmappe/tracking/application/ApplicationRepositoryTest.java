package dev.kafetzi.jobmappe.tracking.application;

import dev.kafetzi.jobmappe.TestcontainersConfiguration;
import dev.kafetzi.jobmappe.tracking.company.Company;
import dev.kafetzi.jobmappe.tracking.jobposting.JobPosting;
import dev.kafetzi.jobmappe.tracking.jobposting.JobPostingStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestcontainersConfiguration.class)
class ApplicationRepositoryTest {
    private static final Pageable PAGEABLE = PageRequest.of(0, 20);

    @Autowired ApplicationRepository applicationRepository;
    @Autowired TestEntityManager entityManager;

    private Company persistCompany(String name) {
        Company company = new Company();
        company.setName(name);
        return entityManager.persist(company);
    }

    private JobPosting persistJobPosting(Company company, String title) {
        JobPosting jobPosting = new JobPosting(company);
        jobPosting.setTitle(title);
        jobPosting.setUrl("https://example.com/job");
        jobPosting.setStatus(JobPostingStatus.ACTIVE);
        return entityManager.persist(jobPosting);
    }

    private Application persistApplication(JobPosting jobPosting) {
        return entityManager.persist(new Application(jobPosting));
    }

    private void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void findAllWithJobPostingAndCompany_searchMatchesJobTitleOrCompanyName() {
        Company acme = persistCompany("Acme");
        Company devCorp = persistCompany("DevCorp");
        Company initech = persistCompany("Initech");
        persistApplication(persistJobPosting(acme, "Developer"));
        persistApplication(persistJobPosting(devCorp, "Tester"));
        persistApplication(persistJobPosting(initech, "Manager"));
        flushAndClear();

        Page<Application> result = applicationRepository.findAllWithJobPostingAndCompany("DEV", PAGEABLE);

        assertThat(result.getContent())
                .extracting(application -> application.getJobPosting().getTitle())
                .containsExactlyInAnyOrder("Developer", "Tester");
    }

    @Test
    void findAllAppliedJobPostingIds_returnsOnlyIdsOfPostingsWithApplication() {
        Company company = persistCompany("Acme");
        JobPosting applied = persistJobPosting(company, "Applied");
        JobPosting notApplied = persistJobPosting(company, "Not applied");
        persistApplication(applied);
        flushAndClear();

        List<UUID> result = applicationRepository.findAllAppliedJobPostingIds(
                List.of(applied.getId(), notApplied.getId())
        );

        assertThat(result).containsExactly(applied.getId());
    }

    @Test
    void findApplicationIdByJobPostingId_returnsIdOnlyIfApplicationExists() {
        Company company = persistCompany("Acme");
        JobPosting applied = persistJobPosting(company, "Applied");
        JobPosting notApplied = persistJobPosting(company, "Not applied");
        Application application = persistApplication(applied);
        flushAndClear();

        Optional<UUID> found = applicationRepository.findApplicationIdByJobPostingId(applied.getId());
        Optional<UUID> missing = applicationRepository.findApplicationIdByJobPostingId(notApplied.getId());

        assertThat(found).contains(application.getId());
        assertThat(missing).isEmpty();
    }
}