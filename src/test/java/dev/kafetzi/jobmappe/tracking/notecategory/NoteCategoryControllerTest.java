package dev.kafetzi.jobmappe.tracking.notecategory;

import dev.kafetzi.jobmappe.tracking.notecategory.dto.NoteCategoryDto;
import dev.kafetzi.jobmappe.tracking.notecategory.dto.NoteCategoryRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(NoteCategoryController.class)
class NoteCategoryControllerTest {
    @Autowired MockMvcTester mvc;
    @MockitoBean NoteCategoryService noteCategoryService;

    @Test
    void getAllNoteCategories_returnsOk() {
        NoteCategoryDto noteCategoryDto = new NoteCategoryDto(UUID.randomUUID(), "Interview");
        when(noteCategoryService.getAllNoteCategories()).thenReturn(List.of(noteCategoryDto));

        assertThat(mvc.get().uri("/api/note-categories"))
                .hasStatusOk()
                .bodyJson().extractingPath("$[0].name").isEqualTo("Interview");
    }

    @Test
    void getNoteCategory_returnsOk() {
        UUID noteCategoryId = UUID.randomUUID();
        when(noteCategoryService.getNoteCategoryById(noteCategoryId))
                .thenReturn(new NoteCategoryDto(noteCategoryId, "Interview"));

        assertThat(mvc.get().uri("/api/note-categories/" + noteCategoryId))
                .hasStatusOk()
                .bodyJson().extractingPath("$.name").isEqualTo("Interview");
    }

    @Test
    void createNoteCategory_returnsCreatedWithLocation() {
        NoteCategoryDto noteCategoryDto = new NoteCategoryDto(UUID.randomUUID(), "Interview");
        when(noteCategoryService.createNoteCategory(any(NoteCategoryRequestDto.class)))
                .thenReturn(noteCategoryDto);

        assertThat(mvc.post().uri("/api/note-categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"name": "Interview"}
                        """))
                .hasStatus(HttpStatus.CREATED)
                .hasHeader("Location", "http://localhost/api/note-categories/" + noteCategoryDto.id())
                .bodyJson().extractingPath("$.name").isEqualTo("Interview");
    }

    @Test
    void createNoteCategory_withBlankName_returnsBadRequest() {
        assertThat(mvc.post().uri("/api/note-categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"name": ""}
                        """))
                .hasStatus(HttpStatus.BAD_REQUEST)
                .bodyJson().extractingPath("$.errors.name").isNotNull();
    }

    @Test
    void updateNoteCategory_returnsOk() {
        UUID noteCategoryId = UUID.randomUUID();
        when(noteCategoryService.updateNoteCategory(eq(noteCategoryId), any(NoteCategoryRequestDto.class)))
                .thenReturn(new NoteCategoryDto(noteCategoryId, "Offer"));

        assertThat(mvc.put().uri("/api/note-categories/" + noteCategoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"name": "Offer"}
                        """))
                .hasStatusOk()
                .bodyJson().extractingPath("$.name").isEqualTo("Offer");
    }

    @Test
    void deleteNoteCategory_returnsNoContent() {
        UUID noteCategoryId = UUID.randomUUID();

        assertThat(mvc.delete().uri("/api/note-categories/" + noteCategoryId))
                .hasStatus(HttpStatus.NO_CONTENT);

        verify(noteCategoryService).deleteNoteCategory(noteCategoryId);
    }
}