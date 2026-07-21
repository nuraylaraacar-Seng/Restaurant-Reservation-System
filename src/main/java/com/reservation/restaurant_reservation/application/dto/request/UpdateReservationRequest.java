package com.reservation.restaurant_reservation.application.dto.request;

import com.reservation.restaurant_reservation.domain.enums.ReservationStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateReservationRequest {

    @Future
    private LocalDateTime startTime;

    @Future
    private LocalDateTime endTime;

    @Min(1)
    private Integer guestCount;

    @Size(max = 500)
    private String notes;

    private ReservationStatus status;
}
