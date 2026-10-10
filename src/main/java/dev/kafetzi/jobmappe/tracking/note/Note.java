package dev.kafetzi.jobmappe.tracking.note;

import dev.kafetzi.jobmappe.tracking.application.Application;
import dev.kafetzi.jobmappe.tracking.notecategory.NoteCategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notes")
@NoArgsConstructor
@Getter
public class Note {
    public Note(Application application) {
        if (application == null) {
            throw new IllegalArgumentException("Application cannot be null");
        }

        this.application = application;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id",  nullable = false)
    private Application application;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "note_category_id")
    private NoteCategory noteCategory;

    @Setter
    @Column(name = "title", nullable = false)
    private String title;

    @Setter
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
