package com.reservation.restaurant_reservation.application.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTableRequest {

    @Min(1)
    private Integer capacity;

    @Size(max = 100)
    private String location;

    private Boolean active;
}
