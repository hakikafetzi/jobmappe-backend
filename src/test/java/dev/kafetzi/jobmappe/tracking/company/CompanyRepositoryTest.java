package dev.kafetzi.jobmappe.tracking.company;

import dev.kafetzi.jobmappe.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestcontainersConfiguration.class)
class CompanyRepositoryTest {
    @Autowired CompanyRepository companyRepository;
    @Autowired TestEntityManager entityManager;

    @Test
    void save_nameDiffersOnlyByCaseAndWhitespace_violatesUniqueIndex() {
        Company existing = new Company();
        existing.setName("Test GmbH");
        entityManager.persist(existing);
        entityManager.flush();
        Company duplicate = new Company();
        duplicate.setName("  test gmbh  ");

        assertThatThrownBy(() -> companyRepository.saveAndFlush(duplicate))
                .isInstanceOf(DataIntegrityViolationException.class)
                .rootCause().hasMessageContaining("ux_companies_name");
    }
}