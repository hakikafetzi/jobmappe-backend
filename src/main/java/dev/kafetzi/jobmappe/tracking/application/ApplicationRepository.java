package dev.kafetzi.jobmappe.tracking.application;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, UUID> {

    @Query("SELECT a FROM Application a JOIN FETCH a.jobPosting jp JOIN FETCH jp.company c")
    List<Application> findAllWithJobPostingAndCompany();

    @Query("SELECT a FROM Application a JOIN FETCH a.jobPosting jp JOIN FETCH jp.company c WHERE a.id = :id")
    Optional<Application> findByIdWithJobPostingAndCompany(@Param("id") UUID id);

    boolean existsApplicationByJobPostingId(UUID jobPostingId);

    @Query("SELECT a.jobPosting.id FROM Application a")
    List<UUID> findAllAppliedJobPostingIds();
}
