package com.reservation.restaurant_reservation.application.mapper;

import com.reservation.restaurant_reservation.application.dto.response.TableResponse;
import com.reservation.restaurant_reservation.domain.entity.RestaurantTable;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TableMapper {

    TableResponse toResponse(RestaurantTable table);

    List<TableResponse> toResponseList(List<RestaurantTable> tables);
}
