package com.example.terrainrental.controller;

import com.example.terrainrental.model.Terrain;
import com.example.terrainrental.repository.TerrainRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/terrains")
public class TerrainController {

    private final TerrainRepository terrainRepository;

    public TerrainController(TerrainRepository terrainRepository) {
        this.terrainRepository = terrainRepository;
    }

    @GetMapping
    public List<Terrain> findAll() {
        return terrainRepository.findAll();
    }

    @GetMapping("/available")
    public List<Terrain> findAvailable() {
        return terrainRepository.findByIsAvailableTrue();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Terrain> findById(@PathVariable Long id) {
        return terrainRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Terrain create(@RequestBody Terrain terrain) {
        return terrainRepository.save(terrain);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Terrain> update(@PathVariable Long id, @RequestBody Terrain payload) {
        return terrainRepository.findById(id).map(existing -> {
            payload.setId(id);
            return ResponseEntity.ok(terrainRepository.save(payload));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!terrainRepository.existsById(id)) return ResponseEntity.notFound().build();
        terrainRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
