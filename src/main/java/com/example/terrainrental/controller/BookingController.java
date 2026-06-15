package com.example.terrainrental.controller;

import com.example.terrainrental.model.Booking;
import com.example.terrainrental.repository.BookingRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin
public class BookingController {

    private final BookingRepository repository;

    public BookingController(BookingRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Booking> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/renter/{renterId}")
    public List<Booking> getByRenter(@PathVariable Long renterId) {
        return repository.findByRenterId(renterId);
    }

    @PostMapping
    public Booking create(@RequestBody Booking booking) {
        return repository.save(booking);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Booking> update(@PathVariable Long id, @RequestBody Booking payload) {
        return repository.findById(id).map(booking -> {
            booking.setStartDate(payload.getStartDate());
            booking.setEndDate(payload.getEndDate());
            booking.setTotalPrice(payload.getTotalPrice());
            if (payload.getStatus() != null) {
                booking.setStatus(payload.getStatus());
            }
            return ResponseEntity.ok(repository.save(booking));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
