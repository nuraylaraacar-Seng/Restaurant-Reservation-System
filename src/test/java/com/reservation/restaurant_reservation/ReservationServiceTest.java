package com.reservation.restaurant_reservation;

import com.reservation.restaurant_reservation.application.dto.request.CreateReservationRequest;
import com.reservation.restaurant_reservation.application.dto.response.ReservationResponse;
import com.reservation.restaurant_reservation.application.mapper.ReservationMapper;
import com.reservation.restaurant_reservation.application.service.ReservationService;
import com.reservation.restaurant_reservation.domain.entity.Reservation;
import com.reservation.restaurant_reservation.domain.entity.RestaurantTable;
import com.reservation.restaurant_reservation.domain.entity.User;
import com.reservation.restaurant_reservation.domain.exception.BusinessException;
import com.reservation.restaurant_reservation.domain.exception.ReservationConflictException;
import com.reservation.restaurant_reservation.domain.exception.ResourceNotFoundException;
import com.reservation.restaurant_reservation.infrastructure.persistence.repository.ReservationRepository;
import com.reservation.restaurant_reservation.infrastructure.persistence.repository.TableRepository;
import com.reservation.restaurant_reservation.infrastructure.persistence.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private TableRepository tableRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private ReservationMapper reservationMapper;

    @InjectMocks
    private ReservationService reservationService;

    private CreateReservationRequest validRequest;
    private User mockUser;
    private RestaurantTable mockTable;

    @BeforeEach
    void setUp() {
        validRequest = new CreateReservationRequest(1L, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(2), 2, "Cam kenarı");
        mockUser = new User();
        mockUser.setId(100L);
        mockTable = new RestaurantTable();
        mockTable.setId(1L);
        mockTable.setCapacity(4);
        mockTable.setActive(true);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn("100");
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void createReservation_ShouldReturnResponse_WhenEverythingIsValid() {
        when(userRepository.findById(100L)).thenReturn(Optional.of(mockUser));
        when(tableRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(mockTable));
        when(reservationRepository.findConflictingForUpdate(anyLong(), any(), any(), any())).thenReturn(Collections.emptyList());

        Reservation savedReservation = new Reservation();
        when(reservationRepository.save(any(Reservation.class))).thenReturn(savedReservation);
        when(reservationMapper.toResponse(savedReservation)).thenReturn(new ReservationResponse());

        ReservationResponse result = reservationService.createReservation(validRequest);

        assertNotNull(result);
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void createReservation_ShouldThrowConflictException_WhenTableIsAlreadyBooked() {
        when(userRepository.findById(100L)).thenReturn(Optional.of(mockUser));
        when(tableRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(mockTable));
        when(reservationRepository.findConflictingForUpdate(anyLong(), any(), any(), any())).thenReturn(List.of(new Reservation()));

        assertThrows(ReservationConflictException.class, () -> reservationService.createReservation(validRequest));
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void createReservation_ShouldThrowNotFoundException_WhenTableDoesNotExist() {
        when(userRepository.findById(100L)).thenReturn(Optional.of(mockUser));
        when(tableRepository.findByIdForUpdate(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reservationService.createReservation(validRequest));
    }

    @Test
    void createReservation_ShouldThrowBusinessException_WhenCapacityIsInsufficient() {
        validRequest.setGuestCount(6);
        when(userRepository.findById(100L)).thenReturn(Optional.of(mockUser));
        when(tableRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(mockTable));

        assertThrows(BusinessException.class, () -> reservationService.createReservation(validRequest));
    }
}