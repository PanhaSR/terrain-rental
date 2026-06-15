package com.example.terrainrental.controller;

import com.example.terrainrental.model.Payment;
import com.example.terrainrental.repository.PaymentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin
public class PaymentController {

    private final PaymentRepository repository;

    public PaymentController(PaymentRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Payment> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/booking/{bookingId}")
    public List<Payment> getByBooking(@PathVariable Long bookingId) {
        return repository.findByBookingId(bookingId);
    }

    @PostMapping
    public Payment create(@RequestBody Payment payment) {
        return repository.save(payment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Payment> update(@PathVariable Long id, @RequestBody Payment payload) {
        return repository.findById(id).map(payment -> {
            payment.setPaymentMethod(payload.getPaymentMethod());
            payment.setAmountPaid(payload.getAmountPaid());
            if (payload.getStatus() != null) {
                payment.setStatus(payload.getStatus());
            }
            payment.setTransactionId(payload.getTransactionId());
            return ResponseEntity.ok(repository.save(payment));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
