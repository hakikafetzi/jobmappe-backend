package dev.kafetzi.jobmappe.tracking.note;

import dev.kafetzi.jobmappe.tracking.note.dto.NoteCompactDto;
import dev.kafetzi.jobmappe.tracking.note.dto.NoteDetailDto;
import dev.kafetzi.jobmappe.tracking.note.dto.NoteRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(NoteController.class)
class NoteControllerTest {
    @Autowired MockMvcTester mvc;
    @MockitoBean NoteService noteService;

    private NoteDetailDto noteDetailDto(UUID id, String title) {
        return new NoteDetailDto(
                id, null, title, null,
                LocalDateTime.now(), LocalDateTime.now(),
                null, null, null
        );
    }

    @Test
    void getAllNotes_returnsOkWithPagedResponse() {
        NoteCompactDto noteCompactDto = new NoteCompactDto(
                UUID.randomUUID(), null, "Interview prep",
                LocalDateTime.now(), LocalDateTime.now(),
                null, null, null
        );
        when(noteService.getAllNotes(any(), any(), any(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(noteCompactDto), PageRequest.of(0, 20), 1));

        assertThat(mvc.get().uri("/api/notes"))
                .hasStatusOk()
                .bodyJson().satisfies(json -> {
                    assertThat(json).extractingPath("$.content[0].title").isEqualTo("Interview prep");
                    assertThat(json).extractingPath("$.totalElements").isEqualTo(1);
                });
    }

    @Test
    void getNote_returnsOk() {
        UUID noteId = UUID.randomUUID();
        when(noteService.getNote(noteId)).thenReturn(noteDetailDto(noteId, "Interview prep"));

        assertThat(mvc.get().uri("/api/notes/" + noteId))
                .hasStatusOk()
                .bodyJson().extractingPath("$.title").isEqualTo("Interview prep");
    }

    @Test
    void createNote_returnsCreatedWithLocation() {
        NoteDetailDto noteDetailDto = noteDetailDto(UUID.randomUUID(), "Interview prep");
        when(noteService.createNote(any(NoteRequestDto.class))).thenReturn(noteDetailDto);

        assertThat(mvc.post().uri("/api/notes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"title": "Interview prep"}
                        """))
                .hasStatus(HttpStatus.CREATED)
                .hasHeader("Location", "http://localhost/api/notes/" + noteDetailDto.id())
                .bodyJson().extractingPath("$.title").isEqualTo("Interview prep");
    }

    @Test
    void createNote_withBlankTitle_returnsBadRequest() {
        assertThat(mvc.post().uri("/api/notes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"title": ""}
                        """))
                .hasStatus(HttpStatus.BAD_REQUEST)
                .bodyJson().extractingPath("$.errors.title").isNotNull();
    }

    @Test
    void updateNote_returnsOk() {
        UUID noteId = UUID.randomUUID();
        when(noteService.updateNote(eq(noteId), any(NoteRequestDto.class)))
                .thenReturn(noteDetailDto(noteId, "Updated title"));

        assertThat(mvc.put().uri("/api/notes/" + noteId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"title": "Updated title"}
                        """))
                .hasStatusOk()
                .bodyJson().extractingPath("$.title").isEqualTo("Updated title");
    }

    @Test
    void deleteNote_returnsNoContent() {
        UUID noteId = UUID.randomUUID();

        assertThat(mvc.delete().uri("/api/notes/" + noteId))
                .hasStatus(HttpStatus.NO_CONTENT);

        verify(noteService).deleteNote(noteId);
    }
}