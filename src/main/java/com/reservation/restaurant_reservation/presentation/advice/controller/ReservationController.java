package com.reservation.restaurant_reservation.presentation.controller;

import com.reservation.restaurant_reservation.application.dto.request.CreateReservationRequest;
import com.reservation.restaurant_reservation.application.dto.response.ReservationResponse;
import com.reservation.restaurant_reservation.application.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ReservationResponse createReservation(@RequestBody CreateReservationRequest request) {
        return reservationService.createReservation(request);
    }
}