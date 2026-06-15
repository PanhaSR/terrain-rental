package com.example.terrainrental.repository;

import com.example.terrainrental.model.Booking;
import com.example.terrainrental.model.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByRenterId(Long renterId);
    List<Booking> findByTerrainId(Long terrainId);
    List<Booking> findByStatus(BookingStatus status);
}
