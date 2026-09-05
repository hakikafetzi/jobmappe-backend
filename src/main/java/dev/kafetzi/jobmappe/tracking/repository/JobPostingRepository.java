package dev.kafetzi.jobmappe.tracking.repository;

import dev.kafetzi.jobmappe.tracking.model.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JobPostingRepository extends JpaRepository<JobPosting, UUID> {

    @Query("SELECT j FROM JobPosting j LEFT JOIN FETCH j.company")
    List<JobPosting> findAllWithCompany();

    @Query("SELECT j FROM JobPosting j LEFT JOIN FETCH j.company WHERE j.id = :id")
    Optional<JobPosting> findByIdWithCompany(@Param("id") UUID id);

    @Query("SELECT j FROM JobPosting j LEFT JOIN FETCH j.company WHERE j.company.id = :companyId")
    List<JobPosting> findAllByCompanyId(@Param("companyId") UUID companyId);

    @Query("SELECT j.company.id, COUNT(j) FROM JobPosting j GROUP BY j.company.id")
    List<Object[]> countJobPostingsPerCompany();

    long countJobPostingsByCompanyId(UUID companyId);
}
