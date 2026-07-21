package com.reservation.restaurant_reservation.domain.entity;

import com.reservation.restaurant_reservation.domain.enums.ReservationStatus;
import com.reservation.restaurant_reservation.domain.exception.BusinessException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reservation extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "table_id", nullable = false)
    private RestaurantTable table;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private Integer guestCount;

    @Column(length = 500)
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private ReservationStatus status = ReservationStatus.CONFIRMED;


    public void cancel() {
        if (this.status == ReservationStatus.COMPLETED) {
            throw new BusinessException("Tamamlanmış rezervasyon iptal edilemez.");
        }
        this.status = ReservationStatus.CANCELLED;
    }

    public void complete() {
        if (this.status != ReservationStatus.CONFIRMED) {
            throw new BusinessException("Sadece onaylanmış rezervasyonlar tamamlanabilir.");
        }
        this.status = ReservationStatus.COMPLETED;
    }
}