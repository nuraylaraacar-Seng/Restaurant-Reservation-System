package com.reservation.restaurant_reservation.application.mapper;

import com.reservation.restaurant_reservation.application.dto.response.ReservationResponse;
import com.reservation.restaurant_reservation.domain.entity.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userEmail", source = "user.email")
    @Mapping(target = "tableId", source = "table.id")
    @Mapping(target = "tableNumber", source = "table.tableNumber")
    ReservationResponse toResponse(Reservation reservation);

    List<ReservationResponse> toResponseList(List<Reservation> reservations);
}
