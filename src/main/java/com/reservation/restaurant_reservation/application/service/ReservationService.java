package com.reservation.restaurant_reservation.application.service;

import com.reservation.restaurant_reservation.application.dto.request.CreateReservationRequest;
import com.reservation.restaurant_reservation.application.dto.response.ReservationResponse;
import com.reservation.restaurant_reservation.application.mapper.ReservationMapper;
import com.reservation.restaurant_reservation.domain.entity.Reservation;
import com.reservation.restaurant_reservation.domain.entity.RestaurantTable;
import com.reservation.restaurant_reservation.domain.entity.User;
import com.reservation.restaurant_reservation.domain.enums.ReservationStatus;
import com.reservation.restaurant_reservation.domain.exception.BusinessException;
import com.reservation.restaurant_reservation.domain.exception.ReservationConflictException;
import com.reservation.restaurant_reservation.domain.exception.ResourceNotFoundException;
import com.reservation.restaurant_reservation.infrastructure.persistence.repository.ReservationRepository;
import com.reservation.restaurant_reservation.infrastructure.persistence.repository.TableRepository;
import com.reservation.restaurant_reservation.infrastructure.persistence.repository.UserRepository;
import com.reservation.restaurant_reservation.infrastructure.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TableRepository tableRepository;
    private final UserRepository userRepository;
    private final ReservationMapper reservationMapper;

    @Transactional
    public ReservationResponse createReservation(CreateReservationRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı bulunamadı."));


        RestaurantTable table = tableRepository.findByIdForUpdate(request.getTableId())
                .orElseThrow(() -> new ResourceNotFoundException("Masa bulunamadı."));

        if (!table.canAcceptGuestCount(request.getGuestCount())) {
            throw new BusinessException("Masa aktif değil veya kapasite yetersiz.");
        }

        List<Reservation> conflicts = reservationRepository.findConflictingForUpdate(
                request.getTableId(),
                request.getStartTime(),
                request.getEndTime(),
                ReservationStatus.CANCELLED
        );

        if (!conflicts.isEmpty()) {
            throw new ReservationConflictException("Seçilen saat aralığında bu masa doludur.");
        }

        Reservation reservation = Reservation.builder()
                .user(user)
                .table(table)
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .guestCount(request.getGuestCount())
                .notes(request.getNotes())
                .status(ReservationStatus.CONFIRMED)
                .build();

        Reservation savedReservation = reservationRepository.save(reservation);
        return reservationMapper.toResponse(savedReservation);
    }
}