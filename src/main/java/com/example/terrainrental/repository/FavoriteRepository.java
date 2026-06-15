package com.example.terrainrental.repository;

import com.example.terrainrental.model.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUserId(Long userId);
    List<Favorite> findByTerrainId(Long terrainId);
    boolean existsByUserIdAndTerrainId(Long userId, Long terrainId);
}
