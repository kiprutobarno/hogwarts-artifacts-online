package com.edu.hogwarts.artifact;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edu.hogwarts.artifact.converter.ArtifactDtoToArtifactConverter;
import com.edu.hogwarts.artifact.converter.ArtifactToArtifactDtoConverter;
import com.edu.hogwarts.artifact.dto.ArtifactDto;
import com.edu.hogwarts.common.Result;
import com.edu.hogwarts.common.StatusCode;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/artifacts")
public class ArtifactController {
    private final ArtifactService artifactService;

    private final ArtifactToArtifactDtoConverter artifactToArtifactDtoConverter;

    private final ArtifactDtoToArtifactConverter artifactDtoToArtifactConverter;

    public ArtifactController(ArtifactService artifactService,
            ArtifactToArtifactDtoConverter artifactToArtifactDtoConverter,
            ArtifactDtoToArtifactConverter artifactDtoToArtifactConverter) {
        this.artifactService = artifactService;
        this.artifactToArtifactDtoConverter = artifactToArtifactDtoConverter;
        this.artifactDtoToArtifactConverter = artifactDtoToArtifactConverter;
    }

    @GetMapping("/{artifactId}")
    public Result findArtifactById(@PathVariable String artifactId) {
        var foundArtifact = this.artifactService.findById(artifactId);
        ArtifactDto artifactDto = this.artifactToArtifactDtoConverter.convert(foundArtifact);
        return new Result(true, StatusCode.SUCCESS, "Find One Success", artifactDto);
    }

    @GetMapping
    public Result findAllArtifacts() {
        List<Artifact> artifacts = this.artifactService.findAll();

        // Convert found artifacts to a list of Dtos
        List<ArtifactDto> res = artifacts
                .stream()
                .map(artifact -> this.artifactToArtifactDtoConverter.convert(artifact))
                .collect(Collectors
                        .toList());
        return new Result(true, StatusCode.SUCCESS, "Find All Success", res);
    }

    @PostMapping("")
    public Result addArtifact(@Valid @RequestBody ArtifactDto artifactDto) {
        // convert artifactDto to artifact
        Artifact artifact = this.artifactDtoToArtifactConverter.convert(artifactDto);
        var savedArtifact = this.artifactService.save(artifact);

        // convert the artifact again back to artifactDto
        var savedArtifactDto = this.artifactToArtifactDtoConverter.convert(savedArtifact);

        return new Result(true, StatusCode.SUCCESS, "Add Success", savedArtifactDto);
    }

    @PutMapping("/{artifactId}")
    public Result updateArtifact(@PathVariable String artifactId, @Valid @RequestBody ArtifactDto artifactDto) {
        Artifact update = this.artifactDtoToArtifactConverter.convert(artifactDto);
        Artifact updatedArtifact = this.artifactService.update(artifactId, update);
        var updatedDto = this.artifactToArtifactDtoConverter.convert(updatedArtifact);
        return new Result(true, StatusCode.SUCCESS, "Update Success", updatedDto);
    }

    @DeleteMapping("/{artifactId}")
    public Result deleteArtifact(@PathVariable String artifactId) {
        this.artifactService.delete(artifactId);
        return new Result(true, StatusCode.SUCCESS, "Delete Success");
    }
}
