package com.example.terrainrental.repository;

import com.example.terrainrental.model.Terrain;
import com.example.terrainrental.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class TerrainRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TerrainRepository terrainRepository;

    private User persistOwner() {
        User owner = new User();
        owner.setName("Test Owner");
        owner.setEmail("test-owner@example.com");
        return userRepository.save(owner);
    }

    private Terrain buildTerrain(User owner, String title, boolean available) {
        Terrain t = new Terrain();
        t.setOwner(owner);
        t.setTitle(title);
        t.setLocation("Phnom Penh");
        t.setAreaSize(new BigDecimal("1000.00"));
        t.setPricePerDay(new BigDecimal("50.00"));
        t.setIsAvailable(available);
        return t;
    }

    @Test
    void savesAndRetrievesTerrain() {
        User owner = persistOwner();
        Terrain saved = terrainRepository.save(buildTerrain(owner, "Pitch A", true));
        assertThat(saved.getId()).isNotNull();
        assertThat(terrainRepository.findById(saved.getId())).isPresent();
    }

    @Test
    void findsOnlyAvailableTerrains() {
        User owner = persistOwner();
        terrainRepository.save(buildTerrain(owner, "Available Pitch", true));
        terrainRepository.save(buildTerrain(owner, "Closed Pitch", false));
        List<Terrain> available = terrainRepository.findByIsAvailableTrue();
        assertThat(available).hasSize(1);
        assertThat(available.get(0).getTitle()).isEqualTo("Available Pitch");
    }

    @Test
    void findsTerrainsByOwner() {
        User owner = persistOwner();
        terrainRepository.save(buildTerrain(owner, "Owned Pitch", true));
        List<Terrain> owned = terrainRepository.findByOwnerId(owner.getId());
        assertThat(owned).hasSize(1);
    }
}
