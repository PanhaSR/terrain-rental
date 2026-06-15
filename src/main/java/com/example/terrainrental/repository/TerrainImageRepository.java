package com.example.terrainrental.repository;

import com.example.terrainrental.model.TerrainImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TerrainImageRepository extends JpaRepository<TerrainImage, Long> {
    List<TerrainImage> findByTerrainId(Long terrainId);
}
