package dev.kafetzi.jobmappe.tracking.jobposting;

import dev.kafetzi.jobmappe.tracking.company.Company;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "job_postings")
@NoArgsConstructor
@Getter
public class JobPosting {
    public JobPosting(Company company) {
        if (company == null) {
            throw new IllegalArgumentException("Company cannot be null");
        }

        this.company = company;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id",  nullable = false)
    private Company company;

    @Setter
    @Column(name = "title", nullable = false)
    private String title;

    @Setter
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Setter
    @Column(name = "location")
    private String location;

    @Setter
    @Column(name = "url", length = 1000, nullable = false)
    private String url;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private JobPostingStatus status;

    @Setter
    @Column(name = "published_at")
    private LocalDate publishedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
