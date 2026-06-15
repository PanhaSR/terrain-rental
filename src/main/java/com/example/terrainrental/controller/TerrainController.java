package com.example.terrainrental.controller;

import com.example.terrainrental.model.Terrain;
import com.example.terrainrental.repository.TerrainRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/terrains")
@CrossOrigin
public class TerrainController {

    private final TerrainRepository terrainRepository;

    public TerrainController(TerrainRepository terrainRepository) {
        this.terrainRepository = terrainRepository;
    }

    @GetMapping
    public List<Terrain> getAll() {
        return terrainRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Terrain> getById(@PathVariable Long id) {
        return terrainRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/available")
    public List<Terrain> getAvailable() {
        return terrainRepository.findByIsAvailableTrue();
    }

    @PostMapping
    public Terrain create(@RequestBody Terrain terrain) {
        return terrainRepository.save(terrain);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Terrain> update(@PathVariable Long id, @RequestBody Terrain payload) {
        return terrainRepository.findById(id).map(terrain -> {
            terrain.setTitle(payload.getTitle());
            terrain.setDescription(payload.getDescription());
            terrain.setLocation(payload.getLocation());
            terrain.setAreaSize(payload.getAreaSize());
            terrain.setPricePerDay(payload.getPricePerDay());
            terrain.setAvailableFrom(payload.getAvailableFrom());
            terrain.setAvailableTo(payload.getAvailableTo());
            if (payload.getIsAvailable() != null) {
                terrain.setIsAvailable(payload.getIsAvailable());
            }
            return ResponseEntity.ok(terrainRepository.save(terrain));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!terrainRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        terrainRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
