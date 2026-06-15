package com.example.terrainrental;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class TerrainRentalApplicationTests {

    @Test
    void contextLoads() {
        // Verifies JPA context loads with H2 in-memory database
    }
}
