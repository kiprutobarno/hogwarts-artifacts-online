package com.edu.hogwarts.artifact;

import java.util.List;

import org.springframework.stereotype.Service;

import com.edu.hogwarts.artifact.utils.IdWorker;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ArtifactService {

    private final ArtifactRepository artifactsDB;

    private final IdWorker idWorker;

    public ArtifactService(ArtifactRepository artifactsDB, IdWorker id) {
        this.artifactsDB = artifactsDB;
        this.idWorker = id;
    }

    public Artifact findById(String artifactId) {
        return artifactsDB.findById(artifactId).orElseThrow(() -> new ArtifactNotFoundException(artifactId));
    }

    public List<Artifact> findAll() {
        return this.artifactsDB.findAll();
    }

    public Artifact save(Artifact newArtifact) {
        newArtifact.setId(idWorker.nextId() + "");
        return this.artifactsDB.save(newArtifact);
    }

    public Artifact update(String artifactId, Artifact update) {
        // Use Fluent Interface: use of method chaining for code legibility

        return this.artifactsDB.findById(artifactId).map(oldArtifact -> {
            // oldArtifact.setId(update.getId());
            oldArtifact.setName(update.getName());
            oldArtifact.setDescription(update.getDescription());
            oldArtifact.setImageUrl(update.getImageUrl());

            Artifact updatedArtifact = this.artifactsDB.save(oldArtifact);
            return updatedArtifact;
        }).orElseThrow(() -> new ArtifactNotFoundException(artifactId));
    }

    public void delete(String artifactId) {
        this.artifactsDB.findById(artifactId)
                .orElseThrow(() -> new ArtifactNotFoundException(artifactId));
        this.artifactsDB.deleteById(artifactId);
    }
}
