package com.example.terrainrental.repository;

import com.example.terrainrental.model.Terrain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TerrainRepository extends JpaRepository<Terrain, Long> {
    List<Terrain> findByOwnerId(Long ownerId);
    List<Terrain> findByIsAvailableTrue();
    List<Terrain> findByLocationContainingIgnoreCase(String location);
}
