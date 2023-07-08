package com.edu.hogwarts.artifact;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.edu.hogwarts.artifact.dto.ArtifactDto;
import com.edu.hogwarts.common.StatusCode;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ArtifactControllerTest {

    @Autowired
    MockMvc mockMvc;

    // Mock a service using MockBean annotation
    @MockBean
    ArtifactService artifactService;

    @Autowired
    ObjectMapper objectMapper;

    List<Artifact> artifacts;

    @BeforeEach
    void setUp() {
        this.artifacts = new ArrayList<>();
        Artifact a = new Artifact();
        a.setId("123493561490098");
        a.setName("Delumniator");
        a.setDescription("Deluminator is a device invented by Albus Dumbledore that resembles a wand");
        a.setImageUrl("imageUrl");
        artifacts.add(a);

        Artifact a1 = new Artifact();
        a1.setId("123493561493485");
        a1.setName("Resurrection Stone");
        a1.setDescription("A resurrection stone allows the holder to bring back a loved one");
        a1.setImageUrl("imageUrl");
        artifacts.add(a1);

        Artifact a2 = new Artifact();
        a2.setId("123493561490098");
        a2.setName("The Marauder's Shop");
        a2.setDescription("A magical map of Hogwarts created by Remus Lupin");
        a2.setImageUrl("imageUrl");
        artifacts.add(a2);

        Artifact a3 = new Artifact();
        a3.setId("123493561493481");
        a3.setName("Elder Wand");
        a3.setDescription("The Elder Wand, known throughout history as the Deathstick");
        a3.setImageUrl("imageUrl");
        artifacts.add(a3);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindArtifactById() throws Exception {

        // Given
        // Define the behaviour of the mock
        given(this.artifactService.findById("123493561490098")).willReturn(artifacts.get(0));

        // When and Then
        this.mockMvc.perform(get("/api/v1/artifacts/123493561490098").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").value("123493561490098"))
                .andExpect(jsonPath("$.data.name").value("Delumniator"));

    }

    @Test
    void testFindArtifactByIdNotFound() throws Exception {

        // Given
        // Define the behaviour of the mock
        given(this.artifactService.findById("123493561490098"))
                .willThrow(new ArtifactNotFoundException("123493561490098"));

        // When and Then
        this.mockMvc.perform(get("/api/v1/artifacts/123493561490098").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Artifact Not Found"))
                .andExpect(jsonPath("$.data").isEmpty());

    }

    @Test
    void testFindAllArtifactsSuccess() throws Exception {
        // Given
        given(this.artifactService.findAll()).willReturn(this.artifacts);

        // When and Then
        this.mockMvc.perform(get("/api/v1/artifacts").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(this.artifacts.size())))
                .andExpect(jsonPath("$.data[0].id").value("123493561490098"))
                .andExpect(jsonPath("$.data[0].name").value("Delumniator"))
                .andExpect(jsonPath("$.data[1].id").value("123493561493485"))
                .andExpect(jsonPath("$.data[1].name").value("Resurrection Stone"));
    }

    @Test
    void testAddArtifactSuccess() throws Exception {
        // Given
        ArtifactDto artifactDto = new ArtifactDto(
                null, "Remembral",
                "A remembral was a large marble-sized glassball that contained that contained smome used when user has forgotten something. It tuned clear once whatever was forgotten was remembered",
                "imageUrl", null);

        // Convert the data to JSON format using Jackson's ObjectMapper to serialize
        String json = objectMapper.writeValueAsString(artifactDto);

        Artifact savedArtifact = new Artifact();
        savedArtifact.setId("123493561493461");
        savedArtifact.setName("Remembral");
        savedArtifact.setDescription(
                "A remembral was a large marble-sized glassball that contained that contained smome used when user has forgotten something. It tuned clear once whatever was forgotten was remembered");

        savedArtifact.setImageUrl("imageUrl");

        given(this.artifactService.save(Mockito.any(Artifact.class))).willReturn(savedArtifact);

        // When and Then
        this.mockMvc
                .perform(post("/api/v1/artifacts").contentType(MediaType.APPLICATION_JSON).content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value(savedArtifact.getName()))
                .andExpect(jsonPath("$.data.description").value(savedArtifact.getDescription()))
                .andExpect(jsonPath("$.data.imageUrl").value(savedArtifact.getImageUrl()));
    }

    @Test
    void testArtifactUpdateSuccess() throws Exception {
        // Given
        ArtifactDto artifactDto = new ArtifactDto(
                "123493561490098",
                "Invisibility Cloak",
                "A new description",
                "imageUrl",
                null);

        // Convert the data to JSON format using Jackson's ObjectMapper to serialize
        String json = objectMapper.writeValueAsString(artifactDto);

        Artifact updatedArtifact = new Artifact();
        updatedArtifact.setId("123493561490098");
        updatedArtifact.setName("Invisibility Cloak");
        updatedArtifact.setDescription("A new description");
        updatedArtifact.setImageUrl("imageUrl");

        given(this.artifactService.update(eq("123493561490098"), Mockito.any(Artifact.class)))
                .willReturn(updatedArtifact);

        // When and Then
        this.mockMvc
                .perform(put("/api/v1/artifacts/123493561490098").contentType(MediaType.APPLICATION_JSON).content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.id").value("123493561490098"))
                .andExpect(jsonPath("$.data.name").value(updatedArtifact.getName()))
                .andExpect(jsonPath("$.data.description").value(updatedArtifact.getDescription()))
                .andExpect(jsonPath("$.data.imageUrl").value(updatedArtifact.getImageUrl()));
    }

    @Test
    void testUpdateArtifactErrorWithNonExistentId() throws Exception {
        // Given
        ArtifactDto artifactDto = new ArtifactDto(
                "123493561490098",
                "Invisibility Cloak",
                "A new description",
                "imageUrl",
                null);

        // Convert the data to JSON format using Jackson's ObjectMapper to serialize
        String json = objectMapper.writeValueAsString(artifactDto);

        given(this.artifactService.update(eq("123493561490098"), Mockito.any(Artifact.class)))
                .willThrow(new ArtifactNotFoundException("123493561490098"));

        // When and Then
        this.mockMvc
                .perform(put("/api/v1/artifacts/123493561490098").contentType(MediaType.APPLICATION_JSON).content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Artifact Not Found"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void deleteArtifactSuccess() throws Exception {

        // Given
        doNothing().when(this.artifactService).delete("123493561490098");

        // When and Then
        this.mockMvc
                .perform(delete("/api/v1/artifacts/123493561490098").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void deleteArtifactErrorWithNonExistentId() throws Exception {

        // Given
        doThrow(new ArtifactNotFoundException("123493561490098")).when(this.artifactService).delete("123493561490098");

        // When and Then
        this.mockMvc
                .perform(delete("/api/v1/artifacts/123493561490098").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Artifact Not Found"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

}
