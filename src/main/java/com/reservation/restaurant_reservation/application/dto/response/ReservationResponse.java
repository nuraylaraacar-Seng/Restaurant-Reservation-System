package com.reservation.restaurant_reservation.application.dto.response;

import com.reservation.restaurant_reservation.domain.enums.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponse {

    private Long id;
    private Long userId;
    private String userEmail;
    private Long tableId;
    private String tableNumber;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer guestCount;
    private String notes;
    private ReservationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
