package com.edu.hogwarts.artifact;

public class ArtifactNotFoundException extends RuntimeException {
    public ArtifactNotFoundException(String id) {
        super("Artifact Not Found");
    }
}
