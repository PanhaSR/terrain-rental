package com.example.terrainrental.repository;

import com.example.terrainrental.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByTerrainId(Long terrainId);
    List<Review> findByUserId(Long userId);
}
