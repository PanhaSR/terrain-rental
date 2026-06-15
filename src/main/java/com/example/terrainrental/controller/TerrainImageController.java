package com.example.terrainrental.controller;

import com.example.terrainrental.model.TerrainImage;
import com.example.terrainrental.repository.TerrainImageRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/terrain-images")
@CrossOrigin
public class TerrainImageController {

    private final TerrainImageRepository repository;

    public TerrainImageController(TerrainImageRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<TerrainImage> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TerrainImage> getById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/terrain/{terrainId}")
    public List<TerrainImage> getByTerrain(@PathVariable Long terrainId) {
        return repository.findByTerrainId(terrainId);
    }

    @PostMapping
    public TerrainImage create(@RequestBody TerrainImage image) {
        return repository.save(image);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TerrainImage> update(@PathVariable Long id, @RequestBody TerrainImage payload) {
        return repository.findById(id).map(image -> {
            image.setImagePath(payload.getImagePath());
            if (payload.getTerrain() != null) {
                image.setTerrain(payload.getTerrain());
            }
            return ResponseEntity.ok(repository.save(image));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
