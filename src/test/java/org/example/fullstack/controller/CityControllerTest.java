package org.example.fullstack.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.fullstack.db.dto.dto.CityDto;
import org.example.fullstack.db.dto.request.CityCreateRequest;
import org.example.fullstack.service.CityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CityController.class)
class CityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CityService cityService;

    @Autowired
    private ObjectMapper objectMapper;

    private CityCreateRequest cityCreateRequest;
    private CityDto cityDto;

    @BeforeEach
    void setUp() {
        cityCreateRequest = new CityCreateRequest(
                "Almaty",
                "Kazakhstan"
        );

        cityDto = new CityDto(
                1L,
                "Almaty",
                "Kazakhstan"
        );
    }

    @Test
    void createCity_ShouldReturnCityDto_WhenValidRequest() throws Exception {
        // Given
        when(cityService.createCity(any(CityCreateRequest.class))).thenReturn(cityDto);

        // When & Then
        mockMvc.perform(post("/cities")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cityCreateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Almaty"))
                .andExpect(jsonPath("$.country").value("Kazakhstan"));
    }

    @Test
    void createCity_ShouldReturnBadRequest_WhenInvalidRequest() throws Exception {
        // Given
        CityCreateRequest invalidRequest = new CityCreateRequest(
                "", // empty name
                "Kazakhstan"
        );

        // When & Then
        mockMvc.perform(post("/cities")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getCity_ShouldReturnCityDto_WhenCityExists() throws Exception {
        // Given
        when(cityService.getCity(1L)).thenReturn(cityDto);

        // When & Then
        mockMvc.perform(get("/cities/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Almaty"))
                .andExpect(jsonPath("$.country").value("Kazakhstan"));
    }

    @Test
    void getAllCities_ShouldReturnListOfCityDtos() throws Exception {
        // Given
        List<CityDto> cities = List.of(cityDto);
        when(cityService.getAllCities()).thenReturn(cities);

        // When & Then
        mockMvc.perform(get("/cities"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Almaty"))
                .andExpect(jsonPath("$[0].country").value("Kazakhstan"));
    }

    @Test
    void getCityByName_ShouldReturnCityDto_WhenCityExists() throws Exception {
        // Given
        when(cityService.getCityByName("Almaty")).thenReturn(cityDto);

        // When & Then
        mockMvc.perform(get("/cities/name/Almaty"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Almaty"))
                .andExpect(jsonPath("$.country").value("Kazakhstan"));
    }

    @Test
    void updateCity_ShouldReturnUpdatedCityDto_WhenValidRequest() throws Exception {
        // Given
        when(cityService.updateCity(anyLong(), any(CityCreateRequest.class))).thenReturn(cityDto);

        // When & Then
        mockMvc.perform(put("/cities/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cityCreateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Almaty"));
    }

    @Test
    void updateCity_ShouldReturnBadRequest_WhenInvalidRequest() throws Exception {
        // Given
        CityCreateRequest invalidRequest = new CityCreateRequest(
                "", // empty name
                "Kazakhstan"
        );

        // When & Then
        mockMvc.perform(put("/cities/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void checkCityExists_ShouldReturnTrue_WhenCityExists() throws Exception {
        // Given
        when(cityService.existsByName("Almaty")).thenReturn(true);

        // When & Then
        mockMvc.perform(get("/cities/exists/Almaty"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(true));
    }

    @Test
    void checkCityExists_ShouldReturnFalse_WhenCityDoesNotExist() throws Exception {
        // Given
        when(cityService.existsByName("Nonexistent")).thenReturn(false);

        // When & Then
        mockMvc.perform(get("/cities/exists/Nonexistent"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(false));
    }

    @Test
    void createCity_ShouldReturnInternalServerError_WhenServiceThrowsException() throws Exception {
        // Given
        when(cityService.createCity(any(CityCreateRequest.class)))
                .thenThrow(new RuntimeException("Service error"));

        // When & Then
        mockMvc.perform(post("/cities")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cityCreateRequest)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getCity_ShouldReturnInternalServerError_WhenServiceThrowsException() throws Exception {
        // Given
        when(cityService.getCity(1L))
                .thenThrow(new RuntimeException("Service error"));

        // When & Then
        mockMvc.perform(get("/cities/1"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getAllCities_ShouldReturnInternalServerError_WhenServiceThrowsException() throws Exception {
        // Given
        when(cityService.getAllCities())
                .thenThrow(new RuntimeException("Service error"));

        // When & Then
        mockMvc.perform(get("/cities"))
                .andExpect(status().isInternalServerError());
    }
}
