package dev.kafetzi.jobmappe.tracking.application;

import dev.kafetzi.jobmappe.exception.ConflictException;
import dev.kafetzi.jobmappe.tracking.jobposting.JobPosting;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "applications")
@NoArgsConstructor
@Getter
public class Application {

    public Application(JobPosting jobPosting) {
        if (jobPosting == null) {
            throw new IllegalArgumentException("Job posting cannot be null.");
        }

        this.jobPosting = jobPosting;
        this.status = ApplicationStatus.IN_PROGRESS;
        this.priority = ApplicationPriority.NONE;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_posting_id", nullable = false)
    private JobPosting jobPosting;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private ApplicationStatus status;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false, length = 50)
    private ApplicationPriority priority;

    @Column(name = "applied_at")
    private LocalDate appliedAt;

    @Setter
    @Column(name = "applied_via")
    private String appliedVia;

    @Setter
    @Column(name = "portal_url", length = 1000)
    private String portalUrl;

    @Column(name = "closed_at")
    private LocalDate closedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public void changeStatus(ApplicationStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Application status cannot be null.");
        }

        if (!this.status.canTransitionTo(status)) {
            throw new ConflictException(
                    "Cannot change application status from " + this.status + " to " + status + "."
            );
        }

        if (status == ApplicationStatus.APPLIED && this.appliedAt == null) {
            this.appliedAt = LocalDate.now();
        } else if (status == ApplicationStatus.IN_PROGRESS) {
            this.appliedAt = null;
            this.appliedVia = null;
            this.portalUrl = null;
        }

        if (status.isClosed() && this.closedAt == null) {
            this.closedAt = LocalDate.now();
        } else if (!status.isClosed()) {
            this.closedAt = null;
        }

        this.status = status;
    }
}
