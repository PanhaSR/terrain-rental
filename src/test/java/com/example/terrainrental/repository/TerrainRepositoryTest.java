package com.example.terrainrental.repository;

import com.example.terrainrental.model.Terrain;
import com.example.terrainrental.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TerrainRepositoryTest {

    @Autowired
    private TerrainRepository terrainRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void savesAndFindsAvailableTerrainByOwner() {
        User owner = userRepository.save(
                User.builder().name("Owner").email("owner@test.com").build());

        Terrain terrain = Terrain.builder()
                .owner(owner)
                .title("Test Field")
                .location("Phnom Penh")
                .areaSize(new BigDecimal("1000.00"))
                .pricePerDay(new BigDecimal("50.00"))
                .isAvailable(true)
                .build();
        terrainRepository.save(terrain);

        List<Terrain> available = terrainRepository.findByIsAvailableTrue();
        assertThat(available).hasSize(1);
        assertThat(available.get(0).getTitle()).isEqualTo("Test Field");
        assertThat(available.get(0).getOwner().getEmail()).isEqualTo("owner@test.com");

        assertThat(terrainRepository.findByOwnerId(owner.getId())).hasSize(1);
    }
}
