package com.example.terrainrental.controller;

import com.example.terrainrental.model.TerrainImage;
import com.example.terrainrental.repository.TerrainImageRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/terrain-images")
public class TerrainImageController {

    private final TerrainImageRepository terrainImageRepository;

    public TerrainImageController(TerrainImageRepository terrainImageRepository) {
        this.terrainImageRepository = terrainImageRepository;
    }

    @GetMapping
    public List<TerrainImage> findAll() {
        return terrainImageRepository.findAll();
    }

    @GetMapping("/terrain/{terrainId}")
    public List<TerrainImage> findByTerrain(@PathVariable Long terrainId) {
        return terrainImageRepository.findByTerrainId(terrainId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TerrainImage> findById(@PathVariable Long id) {
        return terrainImageRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public TerrainImage create(@RequestBody TerrainImage image) {
        return terrainImageRepository.save(image);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!terrainImageRepository.existsById(id)) return ResponseEntity.notFound().build();
        terrainImageRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
