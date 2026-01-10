package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import java.util.Map;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = ru.yandex.practicum.filmorate.FilmorateApplication.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)

class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /films — 400 возвращается, если тело запроса - пустое")
    void addFilm_emptyBody_returnsBadRequest() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /films — 400 возвращается, если название - пустое")
    void addFilm_emptyName_returnsBadRequest() throws Exception {
        String body = objectMapper.writeValueAsString(Map.of(
                "name", " ",
                "description", "desc",
                "releaseDate", LocalDate.of(2000, 1, 1).toString(),
                "duration", 100
        ));
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /films — 400 , если длина описания > 200")
    void addFilm_longDescription_returnsBadRequest() throws Exception {
        String longDesc = "a".repeat(201);
        String body = objectMapper.writeValueAsString(Map.of(
                "name", "Name",
                "description", longDesc,
                "releaseDate", LocalDate.of(2000, 1, 1).toString(),
                "duration", 100
        ));
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /films — 400, если дата релиза ранее 1895-12-28")
    void addFilm_tooEarlyDate_returnsBadRequest() throws Exception {
        String body = objectMapper.writeValueAsString(Map.of(
                "name", "Name",
                "description", "desc",
                "releaseDate", LocalDate.of(1895, 12, 27).toString(),
                "duration", 100
        ));
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /films — 400 при неположительной длительности")
    void addFilm_nonPositiveDuration_returnsBadRequest() throws Exception {
        String body = objectMapper.writeValueAsString(Map.of(
                "name", "Name",
                "description", "desc",
                "releaseDate", LocalDate.of(2000, 1, 1).toString(),
                "duration", 0
        ));
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /films — 200 при валидных данных на границе (desc=200, date=1895-12-28, duration=1)")
    void addFilm_validBoundary_returnsOkAndEcho() throws Exception {
        String desc200 = "a".repeat(200);
        String body = objectMapper.writeValueAsString(Map.of(
                "name", "Name",
                "description", desc200,
                "releaseDate", LocalDate.of(1895, 12, 28).toString(),
                "duration", 1
        ));
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Name"));
    }

    @Test
    @DisplayName("GET /films — 200 и массив (может быть пустым)")
    void getFilms_returnsArray() throws Exception {
        mockMvc.perform(get("/films"))
                .andExpect(status().isOk());
    }
}