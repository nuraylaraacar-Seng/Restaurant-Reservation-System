package com.reservation.restaurant_reservation.presentation.controller;

import com.reservation.restaurant_reservation.application.dto.request.CreateReservationRequest;
import com.reservation.restaurant_reservation.application.dto.response.ReservationResponse;
import com.reservation.restaurant_reservation.application.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ReservationResponse createReservation(@Valid @RequestBody CreateReservationRequest request) {
        return reservationService.createReservation(request);
    }

    // Kullanıcının kendi rezervasyonlarını görmesi için yazılımştır bu endpoint
    @GetMapping("/me")
    public List<ReservationResponse> getMyReservations() {
        return reservationService.getMyReservations();
    }

    // İptal - Reservation.cancel() durumu ve
    // PATCH kullandım çünkü tüm kaynağı değiştirmiyoruz, sadece durumunu.
    @PatchMapping("/{id}/cancel")
    public ReservationResponse cancelReservation(@PathVariable Long id) {
        return reservationService.cancelReservation(id);
    }
}
