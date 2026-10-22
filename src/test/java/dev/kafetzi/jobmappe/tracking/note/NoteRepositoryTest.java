package dev.kafetzi.jobmappe.tracking.note;

import dev.kafetzi.jobmappe.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestcontainersConfiguration.class)
class NoteRepositoryTest {
    @Autowired NoteRepository noteRepository;
    @Autowired TestEntityManager entityManager;

    private Note persistNote(String title, String content) {
        Note note = new Note();
        note.setTitle(title);
        note.setContent(content);
        return entityManager.persist(note);
    }

    @Test
    void findAllWithDetails_searchMatchesTitleOrContentIgnoringCase() {
        persistNote("Invoice question", "Ask about the amount");
        persistNote("Phone call", "Please send the INVOICE again");
        persistNote("Unrelated", null);
        entityManager.flush();
        entityManager.clear();

        Page<Note> result = noteRepository.findAllWithDetails("invoice", PageRequest.of(0, 20));

        assertThat(result.getContent())
                .extracting(Note::getTitle)
                .containsExactlyInAnyOrder("Invoice question", "Phone call");
    }
}