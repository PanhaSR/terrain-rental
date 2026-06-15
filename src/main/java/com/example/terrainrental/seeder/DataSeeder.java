package com.example.terrainrental.seeder;

import com.example.terrainrental.model.*;
import com.example.terrainrental.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Seeds sample data on application startup if the database is empty.
 * Disable with:  app.seeder.enabled=false
 */
@Component
@ConditionalOnProperty(name = "app.seeder.enabled", havingValue = "true", matchIfMissing = true)
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final TerrainRepository terrainRepository;
    private final TerrainImageRepository terrainImageRepository;
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final ReviewRepository reviewRepository;
    private final FavoriteRepository favoriteRepository;

    public DataSeeder(UserRepository userRepository,
                      TerrainRepository terrainRepository,
                      TerrainImageRepository terrainImageRepository,
                      BookingRepository bookingRepository,
                      PaymentRepository paymentRepository,
                      ReviewRepository reviewRepository,
                      FavoriteRepository favoriteRepository) {
        this.userRepository = userRepository;
        this.terrainRepository = terrainRepository;
        this.terrainImageRepository = terrainImageRepository;
        this.bookingRepository = bookingRepository;
        this.paymentRepository = paymentRepository;
        this.reviewRepository = reviewRepository;
        this.favoriteRepository = favoriteRepository;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            System.out.println("[Seeder] Data already present, skipping.");
            return;
        }
        System.out.println("[Seeder] Seeding sample data...");

        // ---- Users ----
        User owner = userRepository.save(User.builder()
                .name("Field Owner").email("owner@example.com").build());
        User renter = userRepository.save(User.builder()
                .name("John Renter").email("renter@example.com").build());

        // ---- Terrains (create first WITHOUT main image to satisfy FK ordering) ----
        Terrain terrainA = terrainRepository.save(Terrain.builder()
                .owner(owner)
                .title("Olympic Stadium Field A")
                .description("Full-size grass football field with floodlights.")
                .location("Phnom Penh, Cambodia")
                .areaSize(new BigDecimal("7140.00"))
                .pricePerDay(new BigDecimal("120.00"))
                .availableFrom(LocalDateTime.now())
                .availableTo(LocalDateTime.now().plusMonths(6))
                .isAvailable(true)
                .build());

        Terrain terrainB = terrainRepository.save(Terrain.builder()
                .owner(owner)
                .title("Riverside 5-a-side Pitch")
                .description("Artificial turf, ideal for small matches.")
                .location("Siem Reap, Cambodia")
                .areaSize(new BigDecimal("800.00"))
                .pricePerDay(new BigDecimal("45.00"))
                .isAvailable(true)
                .build());

        // ---- Terrain images, then link a main image back to each terrain ----
        TerrainImage imgA1 = terrainImageRepository.save(TerrainImage.builder()
                .terrain(terrainA).imagePath("/uploads/terrains/a-1.jpg").build());
        terrainImageRepository.save(TerrainImage.builder()
                .terrain(terrainA).imagePath("/uploads/terrains/a-2.jpg").build());
        TerrainImage imgB1 = terrainImageRepository.save(TerrainImage.builder()
                .terrain(terrainB).imagePath("/uploads/terrains/b-1.jpg").build());

        terrainA.setMainImage(imgA1);
        terrainB.setMainImage(imgB1);
        terrainRepository.saveAll(List.of(terrainA, terrainB));

        // ---- Booking ----
        Booking booking = bookingRepository.save(Booking.builder()
                .terrain(terrainA)
                .renter(renter)
                .startDate(LocalDate.now().plusDays(3))
                .endDate(LocalDate.now().plusDays(4))
                .totalPrice(new BigDecimal("240.00"))
                .status(BookingStatus.APPROVED)
                .build());

        // ---- Payment ----
        paymentRepository.save(Payment.builder()
                .booking(booking)
                .paymentMethod("CREDIT_CARD")
                .amountPaid(new BigDecimal("240.00"))
                .status(PaymentStatus.PAID)
                .transactionId("TXN-100001")
                .build());

        // ---- Review ----
        reviewRepository.save(Review.builder()
                .terrain(terrainA)
                .user(renter)
                .rating(5)
                .comment("Great field, well maintained!")
                .build());

        // ---- Favorite ----
        favoriteRepository.save(Favorite.builder()
                .user(renter)
                .terrain(terrainB)
                .build());

        System.out.println("[Seeder] Done. Users=" + userRepository.count()
                + ", Terrains=" + terrainRepository.count());
    }
}
