package com.example.terrainrental.seeder;

import com.example.terrainrental.model.*;
import com.example.terrainrental.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@Profile("!test")
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final TerrainRepository terrainRepository;
    private final TerrainImageRepository terrainImageRepository;
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final ReviewRepository reviewRepository;
    private final FavoriteRepository favoriteRepository;

    public DatabaseSeeder(UserRepository userRepository,
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
        System.out.println("[Seeder] Seeding data...");

        User owner = new User();
        owner.setName("Sok Dara");
        owner.setEmail("owner@example.com");
        userRepository.save(owner);

        User renter = new User();
        renter.setName("Chan Vibol");
        renter.setEmail("renter@example.com");
        userRepository.save(renter);

        Terrain terrain = new Terrain();
        terrain.setOwner(owner);
        terrain.setTitle("Olympic Football Field");
        terrain.setDescription("Full-size grass pitch near Olympic Stadium.");
        terrain.setLocation("Phnom Penh");
        terrain.setAreaSize(new BigDecimal("7140.00"));
        terrain.setPricePerDay(new BigDecimal("120.00"));
        terrain.setAvailableFrom(LocalDateTime.now());
        terrain.setAvailableTo(LocalDateTime.now().plusMonths(6));
        terrain.setIsAvailable(true);
        terrainRepository.save(terrain);

        Terrain terrain2 = new Terrain();
        terrain2.setOwner(owner);
        terrain2.setTitle("Riverside 5-a-side Court");
        terrain2.setDescription("Artificial turf, floodlit for night games.");
        terrain2.setLocation("Siem Reap");
        terrain2.setAreaSize(new BigDecimal("800.00"));
        terrain2.setPricePerDay(new BigDecimal("45.00"));
        terrain2.setIsAvailable(true);
        terrainRepository.save(terrain2);

        TerrainImage image1 = new TerrainImage();
        image1.setTerrain(terrain);
        image1.setImagePath("/uploads/terrains/olympic-1.jpg");
        terrainImageRepository.save(image1);

        terrain.setMainImage(image1);
        terrainRepository.save(terrain);

        Booking booking = new Booking();
        booking.setTerrain(terrain);
        booking.setRenter(renter);
        booking.setStartDate(LocalDate.now().plusDays(3));
        booking.setEndDate(LocalDate.now().plusDays(5));
        booking.setTotalPrice(new BigDecimal("240.00"));
        booking.setStatus(BookingStatus.APPROVED);
        bookingRepository.save(booking);

        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setPaymentMethod("ABA PayWay");
        payment.setAmountPaid(new BigDecimal("240.00"));
        payment.setStatus(PaymentStatus.PAID);
        payment.setTransactionId("TXN-0001");
        paymentRepository.save(payment);

        Review review = new Review();
        review.setTerrain(terrain);
        review.setUser(renter);
        review.setRating(5);
        review.setComment("Great pitch!");
        reviewRepository.save(review);

        Favorite favorite = new Favorite();
        favorite.setUser(renter);
        favorite.setTerrain(terrain2);
        favoriteRepository.save(favorite);

        System.out.println("[Seeder] Done!");
    }
}
