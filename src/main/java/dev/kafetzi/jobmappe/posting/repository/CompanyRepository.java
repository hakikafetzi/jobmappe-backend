package dev.kafetzi.jobmappe.posting.repository;

import dev.kafetzi.jobmappe.posting.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID> {

    @Query("SELECT c FROM Company c LEFT JOIN FETCH c.jobPostings")
    List<Company> findAllWithJobPostings();

    @Query("SELECT c FROM Company c LEFT JOIN FETCH c.jobPostings WHERE c.id = :id")
    Optional<Company> findByIdWithJobPostings(@Param("id") UUID id);

    boolean existsByNameIgnoreCase(String name);
}
