package com.edu.hogwarts.artifact;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.edu.hogwarts.artifact.utils.IdWorker;
import com.edu.hogwarts.wizard.Wizard;

@ExtendWith(MockitoExtension.class)
public class ArtifactServiceTest {

    @Mock
    ArtifactRepository artifactsDB;

    @Mock
    IdWorker idWorker;

    @InjectMocks
    ArtifactService artifactService;

    List<Artifact> artifacts;

    @BeforeEach
    void setUp() {

        Artifact a = new Artifact();
        a.setId("1643548525115805693");
        a.setName("Delumniator");
        a.setDescription("Deluminator is a device invented by Albus Dumbledore that resembles a wand");
        a.setImageUrl("imageUrl");

        Artifact a1 = new Artifact();
        a1.setId("000000000000001");
        a1.setName("Resurrection Stone");
        a1.setDescription("A resurrection stone allows the holder to bring back a loved one");
        a1.setImageUrl("imageUrl");

        this.artifacts = new ArrayList<>();
        this.artifacts.add(a);
        this.artifacts.add(a1);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindByIdSuccess() {

        // Given step: Arrange inputs and targets.
        // object artifactsDB
        Artifact a = new Artifact();
        a.setId("1643548525115805693");
        a.setName("Magic Wand");
        a.setDescription("A magic wand is used to perform magic");
        a.setImageUrl("imageUrl");

        Wizard w = new Wizard();
        w.setId(2);
        w.setName("Harry Potter");

        a.setOwner(w);

        // Define the behaviour of the mock
        given(artifactsDB.findById("1643548525115805693")).willReturn(Optional.of(a));
        // object

        // When step: Act on the target behaviour. When steps should cover the method to
        // be tested

        Artifact returnedArtifact = artifactService.findById("1643548525115805693");

        // Then step: Also called Assert step. Assserts expected outcomes
        assertThat(returnedArtifact.getId()).isEqualTo(a.getId());
        assertThat(returnedArtifact.getName()).isEqualTo(a.getName());
        assertThat(returnedArtifact.getDescription()).isEqualTo(a.getDescription());
        assertThat(returnedArtifact.getImageUrl()).isEqualTo(a.getImageUrl());

        verify(artifactsDB, times(1)).findById("1643548525115805693");

    }

    @Test
    void testFindbyIdNotFound() {

        // Given
        // Define the behaviour of the mock
        given(artifactsDB.findById(Mockito.anyString())).willReturn(Optional.empty());

        // When

        Throwable thrown = catchThrowable(() -> {
            artifactService.findById("1643548525115805693");
        });

        // Then
        assertThat(thrown).isInstanceOf(ArtifactNotFoundException.class).hasMessage("Artifact Not Found");

        verify(artifactsDB, times(1)).findById("1643548525115805693");

    }

    @Test
    void testFindAllSuccess() {
        // Given
        given(artifactsDB.findAll()).willReturn(this.artifacts);

        // When
        List<Artifact> actualArtifacts = artifactService.findAll();

        // Then
        assertThat(actualArtifacts.size()).isEqualTo(this.artifacts.size());
        verify(artifactsDB, times(1)).findAll();
    }

    @Test
    void testSaveSuccess() {
        // Given
        Artifact artifact = new Artifact();
        artifact.setName("Artifact 3");
        artifact.setDescription("Description");
        artifact.setImageUrl("ImageUrl");

        given(idWorker.nextId()).willReturn(12345678L);
        given(artifactsDB.save(artifact)).willReturn(artifact);

        // When
        Artifact savedArtifact = artifactService.save(artifact);

        // Then
        assertThat(savedArtifact.getId()).isEqualTo("12345678");
        assertThat(savedArtifact.getName()).isEqualTo(artifact.getName());
        assertThat(savedArtifact.getDescription()).isEqualTo(artifact.getDescription());
        assertThat(savedArtifact.getImageUrl()).isEqualTo(artifact.getImageUrl());
        verify(artifactsDB, times(1)).save(artifact);

    }

    @Test
    void testUpdateSuccess() {
        // Given
        Artifact oldArtifact = new Artifact();
        oldArtifact.setId("1643548525115805693");
        oldArtifact.setName("Invisibility Cloak");
        oldArtifact.setDescription("The invisibility cloak is used to make the wearer invisible");
        oldArtifact.setImageUrl("imageUrl");

        Artifact update = new Artifact();
        update.setId("1643548525115805693");
        update.setName("Invisibility Cloak");
        update.setDescription("new description");
        update.setImageUrl("imageUrl");

        // Define the behaviour of the mock
        given(artifactsDB.save(oldArtifact)).willReturn(oldArtifact);
        given(artifactsDB.findById("1643548525115805693")).willReturn(Optional.of(oldArtifact));

        // When
        Artifact updatedArtifact = artifactService.update("1643548525115805693", update);

        // Then
        assertThat(updatedArtifact.getId()).isEqualTo(update.getId());
        assertThat(updatedArtifact.getDescription()).isEqualTo(update.getDescription());

        verify(artifactsDB, times(1)).findById("1643548525115805693");
        verify(artifactsDB, times(1)).save(updatedArtifact);

    }

    @Test
    void testUpdateNotFound() {
        // Given
        Artifact update = new Artifact();
        update.setName("Invisibility Cloak");
        update.setDescription("new description");
        update.setImageUrl("imageUrl");

        // Define the behaviour of the mock
        given(artifactsDB.findById("1643548525115805693")).willReturn(Optional.empty());

        // When
        assertThrows(ArtifactNotFoundException.class, () -> {
            artifactService.update("1643548525115805693", update);
        });

        // Then
        verify(artifactsDB, times(1)).findById("1643548525115805693");

    }

    @Test
    void testDeleteSuccess() {
        // Given
        Artifact artifact = new Artifact();
        artifact.setId("1643548525115805693");
        artifact.setName("Invisibility Cloak");
        artifact.setDescription("The invisibility cloak is used to make the wearer invisible");
        artifact.setImageUrl("imageUrl");

        // Define the behaviour of the mock
        given(artifactsDB.findById("1643548525115805693")).willReturn(Optional.of(artifact));
        doNothing().when(artifactsDB).deleteById("1643548525115805693");

        // When
        artifactService.delete("1643548525115805693");

        verify(artifactsDB, times(1)).deleteById("1643548525115805693");

    }

    @Test
    void testDeleteNotFound() {
        // Given

        // Define the behaviour of the mock
        given(artifactsDB.findById("1643548525115805693")).willReturn(Optional.empty());

        // When
        assertThrows(ArtifactNotFoundException.class, () -> {
            artifactService.delete("1643548525115805693");
        });

        verify(artifactsDB, times(1)).findById("1643548525115805693");

    }

}
